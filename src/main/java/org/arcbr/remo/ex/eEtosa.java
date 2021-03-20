package org.arcbr.remo.ex;

import org.arcbr.remo.app.RedisConnection;
import org.arcbr.remo.db.redis.repository.RemoRedisRepository;
import org.arcbr.remo.db.redis.repository.RemoSerializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class eEtosa {

    @Autowired
    private RemoRedisRepository redisRepository;


    @PostMapping(value = "set", produces = "application/json")
    public void set(@RequestBody User user){

        redisRepository.set("1", user);

    }

    @GetMapping(value = "get", produces = "application/json")
    public ResponseEntity<?> get(@RequestParam("key") String key){
        return new ResponseEntity<>(redisRepository.get(key, User.class), HttpStatus.OK);
    }
}
