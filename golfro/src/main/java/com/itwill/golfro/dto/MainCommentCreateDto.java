package com.itwill.golfro.dto;

import com.itwill.gaebokchi.repository.MainComment;

import lombok.Data;

@Data
public class MainCommentCreateDto {
	private Integer postId;
	private String content;
	private String author;
	
	
	public MainComment toEntity() {
		
		return MainComment.builder().postId(postId).content(content).author(author).build();
	}

}
