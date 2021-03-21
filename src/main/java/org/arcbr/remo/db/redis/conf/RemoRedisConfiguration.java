package org.arcbr.remo.db.redis.conf;

import org.arcbr.remo.app.RedisConnection;
import org.arcbr.remo.db.redis.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoRedisConfiguration {

    @Bean
    public RemoRedisRepository redisRepository(@Value("${remo.redis.storage.policy:object-mapper}") String policy, RedisConnection redisConnection, @Value("${remo.redis.cache.ttl:10800}") Integer ttl){
        if (ttl < -1)
            throw new RuntimeException("TTL can not be less than -1");
        if (policy.equals("object-mapper"))
            return new RemoObjectMapperRepository( redisConnection, ttl );
        else if (policy.equals("json"))
            return new RemoJSONRepository( redisConnection, ttl );
        else if (policy.equals("serialization"))
            return new RemoSerializationRepository( redisConnection, ttl );
        else
            return new RemoObjectMapperRepository( redisConnection, ttl );
    }
}
