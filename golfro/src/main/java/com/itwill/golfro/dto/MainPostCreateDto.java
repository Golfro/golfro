package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MainPostCreateDto {
	private String clubId;
	private String userid;
	private String title;
	private MultipartFile media;
	private String content;
	private String categoryId;
	private double height;
	private double career;
	private double handy;
	private double ironDistance;
	private double driverDistance;
	private String mediaPath;
}
