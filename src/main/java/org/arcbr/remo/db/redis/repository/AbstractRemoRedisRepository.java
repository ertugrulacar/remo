package org.arcbr.remo.db.redis.repository;

import org.arcbr.remo.app.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public abstract class AbstractRemoRedisRepository implements RemoRedisRepository {

    protected RedisConnection redisConnection;
    protected int ttl;
    protected Logger logger;

    public AbstractRemoRedisRepository(RedisConnection redisConnection, int ttl) {
        this.redisConnection = redisConnection;
        this.ttl = ttl;
        this.logger = LoggerFactory.getLogger(RemoRedisRepository.class);
    }

    @Override
    public void delete(String key) {
        Jedis jedis = redisConnection.get();
        jedis.del(key);
        logger.info("Entity " + key + " has removed from cache");
        jedis.close();
    }

    @Override
    public void extend(String key) {
        Jedis jedis = redisConnection.get();
        jedis.expire(key, ttl);
        logger.info("Entity " + key + " has extended for " + ttl + " seconds");
        jedis.close();
    }
}
