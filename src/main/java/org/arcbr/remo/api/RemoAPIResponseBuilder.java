package org.arcbr.remo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RemoAPIResponseBuilder {

    public ResponseEntity<Map<String, Object>> ok(int redisHit, int mongoHit, Object o){
        Map<String, Object> result = getDefault(redisHit, mongoHit);
        result.put("data", o);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> noContent(){
        return new ResponseEntity<>(getDefault(0, 0), HttpStatus.NO_CONTENT);
    }


    private Map<String, Object> getDefault(int redisHit, int mongoHit){
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("redis_hit", redisHit);
        result.put("mongo_hit", mongoHit);
        return result;
    }
}
