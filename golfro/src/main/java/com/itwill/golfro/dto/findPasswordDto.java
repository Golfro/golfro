package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class findPasswordDto {
	private String userid;
	private String email;
	private String phone;
	private String password;
	
	public User toEntity() {
		return User.builder()
				.userid(userid)
				.email(email)
				.phone(phone)
				.password(password)
				.build();
	}
}
