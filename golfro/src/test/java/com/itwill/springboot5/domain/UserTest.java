package com.itwill.springboot5.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.golfro.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class UserTest {

	@Test
	public void testEqualsAndHashCode() {
		User user = User.builder()
							   .id(1L)
							   .userid("dmswls1089")
							   .password("1210")
							   .email("dmswls1089@naver.com")
							   .build();
		log.info("user = {}", user);
		
		assertThat(user).isNotNull();
	}
	
}
