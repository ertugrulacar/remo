package org.arcbr.remo.db.redis.repository;

import org.arcbr.remo.app.RedisConnection;
import org.arcbr.remo.exception.RemoRedisEntityDecodeException;
import org.arcbr.remo.exception.RemoRedisEntityEncodeException;
import org.arcbr.remo.exception.RemoRedisEntityNotFoundException;
import org.arcbr.remo.exception.RemoRedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Base64;

public class RemoSerializationRepository extends AbstractRemoRedisRepository{


    public RemoSerializationRepository(RedisConnection redisConnection, int ttl) {
        super(redisConnection, ttl);
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

            logger.info("Entity " + key + " has cached");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoRedisEntityEncodeException(e.getMessage());
        }finally {
            jedis.close();
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = redisConnection.get();
        try {
            String value = jedis.get(key);
            if (value == null){
                throw new RemoRedisEntityNotFoundException("Entity not found with key: " + key);
            }
            byte[] data = Base64.getDecoder().decode(value);
            ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream( data ) );
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoRedisEntityDecodeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RemoRedisEntityDecodeException(e.getMessage());
        } finally {
            jedis.close();
        }
    }

}
