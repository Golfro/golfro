package com.itwill.golfro.repository;

import java.util.List;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.dto.MainCommentItemDto;

public interface CommentQuerydsl {
	Comment selectCommentById(Long id);
	
	List<MainCommentItemDto> selectCommentsByPostId(Long postId);
	
	List<Comment> selectAllComments(Long postId);
	
	List<Comment> selectCommentsByUserid(String userid);
	
	List<Comment> selectMyCommentsByUserid(String userid); // origin: mycomment-mapper -> id='selectCommentsByUserid'
	
	List<Comment> selectByPostId(Long postId);
	
	long selectCommentCount(Long postId);
}
