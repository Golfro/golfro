package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class UserSignInDto {
	private String userid;
	private String password;

	public User toEntity() {
		return User.builder()
				.userid(userid)
				.password(password)
				.build();
	}
}
