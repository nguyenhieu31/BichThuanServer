package com.shopproject.shopbt.service.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;
    @Value("${JWT.EXPIRATION_ACCESS_TOKEN}")
    private String expirationToken;
    public void saveToken(String key, String value){
        redisTemplate.opsForValue().set(key,value, Long.parseLong(expirationToken), TimeUnit.MINUTES);
    }
    public String getToken(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }
    public void deleteToken(String key){
        redisTemplate.delete(key);
    }
}
