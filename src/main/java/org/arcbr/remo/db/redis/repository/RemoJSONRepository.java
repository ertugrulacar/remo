package org.arcbr.remo.db.redis.repository;

import com.google.gson.Gson;
import org.arcbr.remo.app.RedisConnection;
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
        String value = gson.toJson(o);
        if (ttl != -1)
            jedis.setex(key, ttl, value);
        else
            jedis.set(key, value);
        jedis.close();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = redisConnection.get();
        String val1 = jedis.get(key);
        jedis.close();
        if (val1 == null)
            return null;
        return gson.fromJson(val1, clazz);
    }

    @Override
    public void delete(String key) {
        Jedis jedis = redisConnection.get();
        jedis.del(key);
        jedis.close();
    }


}
