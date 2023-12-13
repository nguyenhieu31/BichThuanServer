package com.shopproject.shopbt.service.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    public void saveDataInRedis(String key, String value, long expires){
        try{
            redisTemplate.opsForValue().set(key,value,expires,TimeUnit.MINUTES);
            redisTemplate.expireAt(key,new Date(System.currentTimeMillis()+ expires));
        }catch(RedisConnectionFailureException e){
            System.out.println(e.getMessage());
        }
    }
    public String getDataFromRedis(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public void deleteDataInRedis(String key){
        redisTemplate.delete(key);
    }
}
