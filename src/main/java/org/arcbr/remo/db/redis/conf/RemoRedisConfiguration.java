package org.arcbr.remo.db.redis.conf;

import org.arcbr.remo.app.RedisConnection;
import org.arcbr.remo.db.redis.repository.RemoJSONRepository;
import org.arcbr.remo.db.redis.repository.RemoObjectMapperRepository;
import org.arcbr.remo.db.redis.repository.RemoRedisRepository;
import org.arcbr.remo.db.redis.repository.RemoSerializationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
public class RemoRedisConfiguration {

    @Bean
    public RemoRedisRepository repository(@Value("${remo.redis.storage.policy:object-mapper}") String policy, RedisConnection redisConnection, @Value("${remo.redis.cache.ttl:10800}") Integer ttl){
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
