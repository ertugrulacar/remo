package org.arcbr.remo.db.redis.repository;

import org.arcbr.remo.app.RedisConnection;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Base64;

public class RemoSerializationRepository implements RemoRedisRepository{
    
    private RedisConnection redisConnection;
    private int ttl;

    public RemoSerializationRepository(RedisConnection redisConnection, int ttl) {
        this.redisConnection = redisConnection;
        this.ttl = ttl;
    }

    @Override
    public void set(String key, Object o) {
        Jedis jedis = redisConnection.get();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            String value = Base64.getEncoder().encodeToString( baos.toByteArray() );
            if (ttl != -1)
                jedis.setex(key, ttl, value);
            else
                jedis.set(key, value);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = redisConnection.get();
        try {
            String value = jedis.get(key);
            byte[] data = Base64.getDecoder().decode(value);
            ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( data ) );
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    @Override
    public void delete(String key) {
        Jedis jedis = redisConnection.get();
        jedis.del(key);
        jedis.close();
    }
}
