package com.shopproject.shopbt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig{
//    @Value("${spring.data.redis.host}")
//    private String redisHost;
//    @Value("${spring.data.redis.port}")
//    private String redisPort;
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(){
//        RedisStandaloneConfiguration configuration= new RedisStandaloneConfiguration();
//        configuration.setPort(Integer.parseInt(redisPort));
//        configuration.setHostName(redisHost);
//        return new LettuceConnectionFactory(configuration);
//    }
//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<Object,Object> template= new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        return template;
//    }
//    @Value("${spring.data.redis.host}")
//    private String redisHost;
//    @Value("${spring.data.redis.port}")
//    private String redisPort;
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(){
//        RedisStandaloneConfiguration configuration= new RedisStandaloneConfiguration();
//        configuration.setPort(Integer.parseInt(redisPort));
//        configuration.setHostName(redisHost);
//        return new LettuceConnectionFactory(configuration);
//    }
    
//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<Object,Object> template= new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        return template;
//    }
}
