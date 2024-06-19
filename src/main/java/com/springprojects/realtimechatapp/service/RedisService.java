package com.springprojects.realtimechatapp.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class RedisService { 
	
	@Autowired
    private final RedisTemplate<String, Object> redisTemplate; 
    
    @PostConstruct
    public void testConnection() {
        try {
        	System.out.println(redisTemplate.getConnectionFactory().getConnection().toString());
            redisTemplate.getConnectionFactory().getConnection().ping();
            System.out.println("Connected to Redis successfully");
        } catch (Exception e) {
            System.err.println("Unable to connect to Redis: " + e.getMessage());
        }
    }
 
    public <V> void set(String key, V value, long timeout, TimeUnit timeUnit) { 
    	System.out.println("Storing message to redis cache: Key[" + key + "]");
        redisTemplate.opsForValue().set(key, value); 
        redisTemplate.expire(key, timeout, timeUnit); 
    } 

    public <V> V get(String key) { 
        return (V) redisTemplate.opsForValue().get(key); 
    } 
 
    public Boolean hasKey(String key) { 
        return redisTemplate.hasKey(key); 
    } 
} 