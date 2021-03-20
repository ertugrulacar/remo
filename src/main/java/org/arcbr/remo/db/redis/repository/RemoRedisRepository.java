package org.arcbr.remo.db.redis.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RemoRedisRepository {

    void set(String key, Object o);

    <T> T get(String key, Class<T> clazz);

}
