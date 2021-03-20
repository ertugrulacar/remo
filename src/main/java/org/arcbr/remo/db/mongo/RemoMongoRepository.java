package org.arcbr.remo.db.mongo;

import org.arcbr.remo.model.RemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RemoMongoRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<? extends RemoModel> find(String objectId, List<? extends AggregationOperation> aggregationOperations, String collectionName, Class<? extends RemoModel> clazz){
        List<AggregationOperation> opts = new ArrayList<>(aggregationOperations);
        opts.add(0, initialOperation(objectId));
        Aggregation aggregation = Aggregation.newAggregation(opts);
        return reactiveMongoTemplate.aggregate(aggregation, collectionName, clazz);
    }

    private MatchOperation initialOperation(String id){
        return Aggregation.match(Criteria.where("_id").is(id));
    }
}
