package org.arcbr.remo.db.redis.repository;

import com.google.gson.Gson;
import org.arcbr.remo.app.RedisConnection;
import org.arcbr.remo.exception.RemoRedisEntityDecodeException;
import org.arcbr.remo.exception.RemoRedisEntityEncodeException;
import org.arcbr.remo.exception.RemoRedisEntityNotFoundException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

public class RemoJSONRepository implements RemoRedisRepository{

    private RedisConnection redisConnection;
    private int ttl;
    private Gson gson;

    public RemoJSONRepository(RedisConnection redisConnection, int ttl) {
        this.redisConnection = redisConnection;
        this.ttl = ttl;
        this.gson = new Gson();;
    }


    @Override
    public void set(String key, Object o) {
        Jedis jedis = redisConnection.get();
        String value;
        try{
            value = gson.toJson(o);
        }catch (Exception e){
            throw new RemoRedisEntityEncodeException(e.getMessage());
        }
        if (ttl != -1)
            jedis.setex(key, ttl, value);
        else
            jedis.set(key, value);
        jedis.close();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = redisConnection.get();
        String value = jedis.get(key);
        jedis.close();
        if (value == null)
            throw new RemoRedisEntityNotFoundException("Entity not found with key: " + key);
        try{
            return gson.fromJson(value, clazz);
        }catch (Exception e){
            throw new RemoRedisEntityDecodeException(e.getMessage());
        }
    }

    @Override
    public void delete(String key) {
        Jedis jedis = redisConnection.get();
        jedis.del(key);
        jedis.close();
    }


}
