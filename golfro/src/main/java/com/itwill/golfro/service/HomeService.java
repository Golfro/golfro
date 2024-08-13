package com.itwill.golfro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeService {
	
	private final UserRepository userRepo;
	
	@Transactional(readOnly = true)
	public List<User> pointsRank() {
		log.info("pointsRank()");
		List<User> user = userRepo.getUsersPointRank();
		return user;	
	}
	
}
