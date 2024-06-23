package com.springprojects.realtimechatapp.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
public class RedisConfig {
	
	@Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${redis.password:}")  // empty string as default if not provided
    private String redisPassword;
	
	@Bean
   public RedisConnectionFactory redisConnectionFactory() {
		System.out.println("Creating RedisConnectionFactory");
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
	    redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        
        if (!redisPassword.isEmpty()) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        }
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
   }
   @Bean
   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
   	System.out.println("Creating RedisTemplate");
       RedisTemplate<String, Object> template = new RedisTemplate<>();
       template.setConnectionFactory(redisConnectionFactory);
       template.setKeySerializer(new StringRedisSerializer());
       template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
       return template;
   }
}

