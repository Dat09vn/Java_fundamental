package com.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	RedisTemplate<String, Product> productRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Product> template = new RedisTemplate<>();
		JacksonJsonRedisSerializer<Product> productSerializer = new JacksonJsonRedisSerializer<>(Product.class);
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(productSerializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(productSerializer);
		template.afterPropertiesSet();
		return template;
	}
}