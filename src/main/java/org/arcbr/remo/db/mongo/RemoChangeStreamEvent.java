package org.arcbr.remo.db.mongo;

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
    private Logger logger;

    private List<? extends AggregationOperation> aggregationOperations;
    private String collectionName;
    private Criteria filter;
    private Class<? extends RemoModel> clazz;
    private Consumer<ChangeStreamEvent<? extends RemoModel>> consumer;

    public void run(String collectionName, Class<? extends RemoModel> clazz){
        this.collectionName = collectionName;
        this.clazz = clazz;
        logger = LoggerFactory.getLogger(RemoChangeStreamEvent.class);
        reactiveMongoTemplate.changeStream(clazz).watchCollection(collectionName).listen().subscribe(createPrimitiveConsumer());
        logger.info("Collection " + collectionName + " is listening...");
    }

    public void run(String collectionName, Criteria filter, Class<? extends RemoModel> clazz){
        this.collectionName = collectionName;
        this.filter = filter;
        this.clazz = clazz;
    }

    public void run(String collectionName, Class<? extends RemoModel> clazz, List<? extends AggregationOperation> aggregationOperations){
        this.collectionName = collectionName;
        this.clazz = clazz;
        this.aggregationOperations = aggregationOperations;
    }

    public void run(String collectionName, Criteria filter, Class<? extends RemoModel> clazz, List<? extends AggregationOperation> aggregationOperations){
        this.collectionName = collectionName;
        this.filter = filter;
        this.clazz = clazz;
        this.aggregationOperations = aggregationOperations;
    }


    private MatchOperation getMatchOperation(String id){
        return Aggregation.match(Criteria.where("id").is(id));
    }

    private Aggregation getFullAggregation(MatchOperation matchOperation){
        List<AggregationOperation> opts = new ArrayList<>(aggregationOperations);
        opts.add(0, matchOperation);
        return Aggregation.newAggregation(opts);
    }

    private Consumer<ChangeStreamEvent<? extends RemoModel>> createPrimitiveConsumer(){
        if ( consumer != null )
            throw new RemoMongoInvalidStreamEventException("Only one change stream event can be settled!", "MULTIPLE_STREAM_EVENT");
        consumer = event -> {
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
        return consumer;
    }


}
