package com.itwill.golfro.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final UserRepository userRepo;
	private final PostRepository postRepo;
	
//	@Cacheable(key = "#id", value="pointsRank")
	@Transactional(readOnly = true)
	public List<User> pointsRank() {
		log.info("pointsRank()");
		List<User> user = userRepo.getUsersPointRank();
		return user;	
	}
	
//	@Cacheable(key = "#id", value="likesRank")
	public List<Post> likesRank(){
		log.info("likesRank()");
		List<Post> likesRank = postRepo.getUsersLikesRank();
		return likesRank;
	}
	
}
