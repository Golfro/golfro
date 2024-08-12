package com.itwill.golfro.dto;

import lombok.Data;

@Data
public class MainCommentCreateDto {
	private Long postId;
	private String content;
	private String userid;
}
