package com.itwill.golfro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQuerydsl {

}
