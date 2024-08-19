package com.itwill.golfro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostQuerydsl {

}
