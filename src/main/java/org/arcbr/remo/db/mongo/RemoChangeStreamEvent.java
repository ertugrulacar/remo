package org.arcbr.remo.db.mongo;

import com.google.gson.Gson;
import com.mongodb.client.model.changestream.OperationType;
import org.arcbr.remo.db.redis.repository.RemoRedisRepository;
import org.arcbr.remo.exception.RemoMongoInvalidStreamEventException;
import org.arcbr.remo.model.RemoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@Component
@Scope("prototype")
public final class RemoChangeStreamEvent {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    private RemoRedisRepository redisRepository;
    private Logger logger = LoggerFactory.getLogger(RemoChangeStreamEvent.class);

    private List<? extends AggregationOperation> aggregationOperations;
    private String collectionName;
    private Class<? extends RemoModel> clazz;
    private boolean isConsumerCreated;

    public void run(String collectionName, Class<? extends RemoModel> clazz){
        this.collectionName = collectionName;
        this.clazz = clazz;

        reactiveMongoTemplate.changeStream(clazz).watchCollection(collectionName).listen().subscribe( createPrimitiveConsumer() );
        logger.info("Collection " + collectionName + " is listening...");
    }

    public void run(String collectionName, Criteria filter, Class<? extends RemoModel> clazz){
        this.collectionName = collectionName;
        this.clazz = clazz;

        reactiveMongoTemplate.changeStream(clazz).watchCollection(collectionName).filter( filter ).listen().subscribe( createPrimitiveConsumer() );
        logger.info("Collection " + collectionName + " is listening...");
    }

    public void run(String collectionName, Class<? extends RemoModel> clazz, List<? extends AggregationOperation> aggregationOperations){
        this.collectionName = collectionName;
        this.clazz = clazz;
        this.aggregationOperations = aggregationOperations;

        reactiveMongoTemplate.changeStream(clazz).watchCollection(collectionName).listen().subscribe( createAggregatedConsumer() );
        logger.info("Collection " + collectionName + " is listening...");

    }

    public void run(String collectionName, Criteria filter, Class<? extends RemoModel> clazz, List<? extends AggregationOperation> aggregationOperations){
        this.collectionName = collectionName;
        this.clazz = clazz;
        this.aggregationOperations = aggregationOperations;


        reactiveMongoTemplate.changeStream(clazz).watchCollection(collectionName).filter( filter ).listen().subscribe( createAggregatedConsumer() );
        logger.info("Collection " + collectionName + " is listening...");

    }


    private MatchOperation getMatchOperation(String id){
        return Aggregation.match(Criteria.where("_id").is(id));
    }

    private Aggregation getFullAggregation(MatchOperation matchOperation){
        List<AggregationOperation> opts = new ArrayList<>(aggregationOperations);
        opts.add(0, matchOperation);
        return Aggregation.newAggregation(opts);
    }

    private Consumer<ChangeStreamEvent<? extends RemoModel>> createPrimitiveConsumer(){
        if ( isConsumerCreated )
            throw new RemoMongoInvalidStreamEventException("Only one change stream event can be settled!", "MULTIPLE_STREAM_EVENT");
        isConsumerCreated = true;
        return event -> {
            if (event.getOperationType().equals(OperationType.DELETE)){
                String key = collectionName.concat(":").concat(event.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toHexString());
                redisRepository.delete(key);
                logger.info("Entity " + key + " has removed from cache");
            }else if(event.getOperationType().equals(OperationType.INSERT) || event.getOperationType().equals(OperationType.UPDATE) || event.getOperationType().equals(OperationType.REPLACE)) {
                String key = collectionName.concat(":").concat(event.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toHexString());
                redisRepository.set(key, event.getBody());
                logger.info("Entity " + key + " has cached");
            }
        };
    }

    private Consumer<ChangeStreamEvent<? extends RemoModel>> createAggregatedConsumer(){
        if ( isConsumerCreated )
            throw new RemoMongoInvalidStreamEventException("Only one change stream event can be settled!", "MULTIPLE_STREAM_EVENT");

        isConsumerCreated = true;

        return event -> {
            if (event.getOperationType().equals(OperationType.DELETE)){
                String key = collectionName.concat(":").concat(event.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toHexString());
                redisRepository.delete(key);
                logger.info("Entity " + key + " has removed from cache");
            }else if(event.getOperationType().equals(OperationType.INSERT) || event.getOperationType().equals(OperationType.UPDATE) || event.getOperationType().equals(OperationType.REPLACE)) {
                String id = event.getRaw().getDocumentKey().get("_id").asObjectId().getValue().toHexString();
                String key = collectionName.concat(":").concat( id );
                Flux<? extends RemoModel> result = reactiveMongoTemplate.aggregate( getFullAggregation( getMatchOperation( id ) ), collectionName, clazz );
                result.subscribe( entity -> {
                    System.out.println(new Gson().toJson(entity));
                    redisRepository.set(key, entity);
                });
                logger.info("Entity " + key + " has cached");
            }
        };
    }




}
