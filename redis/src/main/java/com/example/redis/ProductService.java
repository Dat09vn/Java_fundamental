package com.example.redis;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	private static final String KEY_PREFIX = "product:";

	private final RedisTemplate<String, Product> redisTemplate;

	public ProductService(RedisTemplate<String, Product> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Product save(Product product) {
		redisTemplate.opsForValue().set(key(product.id()), product);
		return product;
	}

	public Product findById(String id) {
		return redisTemplate.opsForValue().get(key(id));
	}

	public List<Product> findAll() {
		return redisTemplate.keys(KEY_PREFIX + "*").stream()
				.map(redisTemplate.opsForValue()::get)
				.toList();
	}

	public boolean delete(String id) {
		return Boolean.TRUE.equals(redisTemplate.delete(key(id)));
	}

	private String key(String id) {
		return KEY_PREFIX + id;
	}
}