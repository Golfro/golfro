package com.itwill.golfro.repository;

import java.util.List;

import com.itwill.golfro.domain.Comment;

public interface CommentQuerydsl {
	Comment selectCommentById(Long id);
	
	List<Comment> selectCommentsByPostId(Long postId);
	
	List<Comment> selectCommentsByUserid(String userid);
	
	List<Comment> selectMyCommentsByUserid(String userid); // origin: mycomment-mapper -> id='selectCommentsByUserid'
	
	List<Comment> selectByPostId(Long postId);
	
	long selectCommentCount(Long postId);
}
