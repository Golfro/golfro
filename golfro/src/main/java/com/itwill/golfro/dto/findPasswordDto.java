package com.itwill.golfro.dto;

import lombok.Data;

@Data
public class findPasswordDto {
	private String userid;
	private String email;
	private String phone;
	private String password;
}
