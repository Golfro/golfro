package com.itwill.golfro.dto;

import lombok.Data;

@Data
public class CommPostCreateDto {
	private String userid;
	private String title;
	private String media;
	private String content;
	private String categoryId;
}
