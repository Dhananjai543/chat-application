package com.springprojects.realtimechatapp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springprojects.realtimechatapp.entity.ChatMessage;
import com.springprojects.realtimechatapp.entity.MessageType;

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

	public <V> void set(String key, V value, long timeout, TimeUnit timeUnit) {
		System.out.println("Storing message to redis cache: Key[" + key + "]");
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, timeout, timeUnit);
	}

	public <V> void setWithoutExpiration(String key, V value) {
		System.out.println("Storing message to redis cache without expiration: Key[" + key + "]");
		redisTemplate.opsForValue().set(key, value);
	}

	public <V> V get(String key) {
		return (V) redisTemplate.opsForValue().get(key);
	}

	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	public boolean hasKeyLike(String keyword) {
		Set<String> keys = redisTemplate.keys(keyword + "*");
		return keys != null && !keys.isEmpty();
	}

	public List<String> getMessagesByKeyword(String keyword) {
		log.info("Trying to retrieve messages from redis. Keyword = [" + keyword + "]");
		Set<String> keys = redisTemplate.keys(keyword + "*");
		if (keys == null || keys.isEmpty()) {
			log.info("Empty key set");
			return Collections.emptyList();
		}

		List<String> sortedKeys = new ArrayList<>();
		sortedKeys.addAll(keys);
		Collections.sort(sortedKeys, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				// Extract the numeric part after the '-' character
				int numA = Integer.parseInt(a.substring(a.indexOf('-') + 1));
				int numB = Integer.parseInt(b.substring(b.indexOf('-') + 1));

				return Integer.compare(numA, numB);
			}
		});

		List<String> messages = new ArrayList<>();
		for (String key : sortedKeys) {
			String message = redisTemplate.opsForValue().get(key).toString();
			System.out.println("Decrypted message for key [" + key + "] : " + message);
			messages.add(message);
		}
		return messages;
	}

}