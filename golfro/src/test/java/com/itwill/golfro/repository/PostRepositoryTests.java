package com.itwill.golfro.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

@SpringBootTest
public class PostRepositoryTests {
	
	@Autowired
	private PostRepository postRepo;
	
	@Transactional
	@Test
	public void test() {
		List<Post> list = postRepo.findAll();
		
		Post p1 = list.get(1);
 		System.out.println(p1);
		
		User user = p1.getUser();
		System.out.println(user.getNickname());
	}

}
