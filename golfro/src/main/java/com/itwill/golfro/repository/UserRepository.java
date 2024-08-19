package com.itwill.golfro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, UserQuerydsl {
	User findByUserid(String userid);
}
