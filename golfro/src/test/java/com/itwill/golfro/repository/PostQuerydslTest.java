package com.itwill.golfro.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class PostQuerydslTest {

	@Autowired
	private PostRepository postRepo;
	
	@Test
	public void test() {
		long count = postRepo.selectTotalMyPostCount("dmswls1089");
		log.info("count = {}", count);
		
		assertThat(count).isEqualTo(0);
	}
	
}
