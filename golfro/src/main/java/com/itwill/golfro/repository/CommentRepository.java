package com.itwill.golfro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQuerydsl {
	List<Comment> findByPostId(Long postId);
}
