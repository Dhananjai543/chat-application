package com.springprojects.realtimechatapp.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {

	private final ObjectMapper obj = new ObjectMapper();

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

	public void pushToZSet(String key, String value, long ttlInMinutes) {
		long expiryTimestamp = Instant.now().getEpochSecond() + TimeUnit.MINUTES.toSeconds(ttlInMinutes);
		redisTemplate.opsForZSet().add(key, value, expiryTimestamp);
	}

	public <V> void pushAllToZSet(String key, List<V> list, long ttlInMinutes) {
		for(V v : list) {
			long expiryTimestamp = Instant.now().getEpochSecond() + TimeUnit.MINUTES.toSeconds(ttlInMinutes);
			redisTemplate.opsForZSet().add(key, v, expiryTimestamp);
		}

	}

	public void removeExpiredMessages(String key) {
		System.out.println("Removing expired messages from redis for key [" + key + "]");
		long currentTimestamp = Instant.now().getEpochSecond();
		redisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTimestamp);
	}

	public List<String> getNonExpiredMessages(String key) {
		long currentTimestamp = Instant.now().getEpochSecond();
		Set<Object> set = redisTemplate.opsForZSet().rangeByScore(key, currentTimestamp, Double.MAX_VALUE);
		List<String> ans = new ArrayList<>();
		assert set != null;
		for(Object o : set) ans.add(o.toString());
		return ans;
	}

	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}


}