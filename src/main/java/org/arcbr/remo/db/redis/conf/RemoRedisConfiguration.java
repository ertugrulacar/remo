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
    public RemoRedisRepository repository(@Value("${remo.redis.storage.policy:object-mapper}") String policy, RedisConnection redisConnection){
        if (policy.equals("object-mapper"))
            return new RemoObjectMapperRepository( redisConnection );
        else if (policy.equals("json"))
            return new RemoJSONRepository( redisConnection );
        else if (policy.equals("serialization"))
            return new RemoSerializationRepository( redisConnection );
        else
            return new RemoObjectMapperRepository( redisConnection );
    }
}
