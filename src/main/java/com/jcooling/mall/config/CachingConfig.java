package com.jcooling.mall.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @data: 2022/4/6
 * @time: 0:04
 * @description: nothing.
 * @version: 1.0
 */
    @Configuration
    @EnableCaching
    public class CachingConfig {

        @Bean
        public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
            RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
            //设置缓存超时时间
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(30));
            RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
            return redisCacheManager;
        }
}
