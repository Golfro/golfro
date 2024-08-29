//package com.itwill.golfro.config;
//
//import java.time.Duration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Configuration
//public class CacheConfig {
//	
//	@Autowired
//	ObjectMapper objectMapper;
//	
//	@Autowired // 등록했던 캐쉬팩토리
//	RedisConnectionFactory connectionFactory;
//	
//	@Bean
//	CacheManager cacheManager() {
//		RedisCacheConfiguration redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//				.entryTtl(Duration.ofSeconds(60)); // TTL 적용
//		RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory) // Connect 적용
//				.cacheDefaults(redisConfiguration).build();  // 캐쉬 설정과 관련된 사항
//		return redisCacheManager;
//	}
//	
//}
