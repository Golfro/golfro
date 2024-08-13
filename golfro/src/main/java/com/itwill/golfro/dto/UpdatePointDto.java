package com.itwill.golfro.dto;

import lombok.Data;

@Data
public class UpdatePointDto {
	private String userid;
	private String password;
	private Long withdraw;
	private Long point;
}
