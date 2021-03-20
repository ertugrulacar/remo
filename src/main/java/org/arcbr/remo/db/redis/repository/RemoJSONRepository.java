package org.arcbr.remo.db.redis.repository;

import com.google.gson.Gson;
import org.arcbr.remo.app.RedisConnection;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RemoJSONRepository implements RemoRedisRepository{

    private RedisConnection redisConnection;
    private Gson gson;

    public RemoJSONRepository(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
        this.gson = new Gson();;
    }


    @Override
    public void set(String key, Object o) {
        Jedis jedis = redisConnection.get();
        jedis.set(key, gson.toJson(o));
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



}
