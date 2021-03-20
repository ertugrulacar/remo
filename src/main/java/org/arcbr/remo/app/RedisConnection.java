package org.arcbr.remo.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;


@Configuration
public class RedisConnection {


    @Value("${remo.redis.host}")
    private String host;
    @Value("${remo.redis.port}")
    private Integer port;
    @Value("${remo.redis.password:@null}")
    private String password;
    @Value("${remo.redis.database:11}")
    private Integer database;



    private final JedisPoolConfig poolConfig;
    public JedisPool jedisPool;

    public RedisConnection(@Value("${remo.redis.host}") String host) {
        poolConfig = new JedisPoolConfig();
        buildPoolConfig();
        jedisPool = new JedisPool(poolConfig, host);
    }


    public Jedis get(){
        Jedis jedis = jedisPool.getResource();
        if (!password.equals("@null"))
            jedis.auth(password);
        jedis.select(database);
        return jedis;
    }


    private void buildPoolConfig(){
        this.poolConfig.setMaxTotal(128);
        this.poolConfig.setMaxIdle(128);
        this.poolConfig.setMinIdle(16);
        this.poolConfig.setTestOnBorrow(true);
        this.poolConfig.setTestOnReturn(true);
        this.poolConfig.setTestWhileIdle(true);
        this.poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        this.poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        this.poolConfig.setNumTestsPerEvictionRun(3);
        this.poolConfig.setBlockWhenExhausted(true);
    }

}
