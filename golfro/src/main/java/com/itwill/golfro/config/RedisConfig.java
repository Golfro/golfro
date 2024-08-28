//package com.itwill.golfro.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Configuration
//public class RedisConfig {
//	
//	@Value("${spring.redis.port}")
//	public int port;
//	
//	@Value("${spring.redis.host}")
//	public String host;
//	
//	@Value("${spring.redis.password:}")  // 비밀번호 설정 (비밀번호가 없으면 빈 문자열로 설정)
//    private String password;
//	
//	@Autowired
//	public ObjectMapper objectMapper;
//	
//	@Bean // Key Serializer, Value Serializer를 통해서 실제 데이터를 변환하는 과정이 필요하다.
//	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 객체를 json 형태로 변환
//		redisTemplate.setConnectionFactory(redisConnectionFactory);
//		return redisTemplate;    
//	}
//	
//	@Bean // Connect와 관련된 객체. HostName, Port 등을 지정하고, Lettuce 까지 지정
//	RedisConnectionFactory redisConnectionFactory() {
//		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//		redisStandaloneConfiguration.setHostName(host);
//		redisStandaloneConfiguration.setPort(port);
//		redisStandaloneConfiguration.setPassword(password); // 비밀번호 설정
//		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//		return connectionFactory;
//	}
//	
//}
