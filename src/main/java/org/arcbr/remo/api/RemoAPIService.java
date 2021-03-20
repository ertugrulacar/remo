package org.arcbr.remo.api;

import org.arcbr.remo.db.InitialSources;
import org.arcbr.remo.db.mongo.RemoChangeStreamEvent;
import org.arcbr.remo.db.mongo.RemoMongoRepository;
import org.arcbr.remo.db.redis.repository.RemoRedisRepository;
import org.arcbr.remo.exception.RemoRedisEntityNotFoundException;
import org.arcbr.remo.exception.api.BadRequestException;
import org.arcbr.remo.exception.api.UnknownErrorException;
import org.arcbr.remo.model.RemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Service
public class RemoAPIService {

    @Autowired
    private RemoRedisRepository redisRepository;
    @Autowired
    private InitialSources initialSources;
    @Autowired
    private RemoAPIResponseBuilder responseBuilder;
    @Autowired
    private RemoMongoRepository mongoRepository;

    public ResponseEntity<?> get(String collectionName, String objectId) {
        if (!StringUtils.hasText( collectionName ))
            throw new BadRequestException("Invalid collection name", "PRECONDITION_FAILED");
        if (!StringUtils.hasText( objectId ))
            throw new BadRequestException("Invalid objectId", "PRECONDITION_FAILED");

        RemoChangeStreamEvent event = initialSources.getOutputClass(collectionName);
        if (event == null)
            throw new BadRequestException("Unknown collection name", "UNKNOWN_COLLECTION");
        try{
            String key = collectionName + ":" + objectId;
            Object o = redisRepository.get(key, event.clazz);
            redisRepository.extend(key);
            return responseBuilder.ok(1, -1, o);
        }catch (RemoRedisEntityNotFoundException e){
            Flux<? extends RemoModel> flux = mongoRepository.find(objectId, event.aggregationOperations, event.collectionName, event.clazz);
            RemoModel model = flux.blockFirst();
            if (model == null)
                return responseBuilder.noContent();
            redisRepository.set(collectionName + ":" + objectId, model);
            return responseBuilder.ok(0, 1, model);
        }catch (Exception e){
            e.printStackTrace();
            throw new UnknownErrorException("Unknown Error");
        }
    }



}
