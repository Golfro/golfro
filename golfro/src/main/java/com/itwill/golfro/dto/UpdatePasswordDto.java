package com.itwill.golfro.dto;

import com.itwill.gaebokchi.repository.User;

import lombok.Data;

@Data
public class UpdatePasswordDto {
	private String userid;
	private String email;
	private String phone;
	private String password;

	public User toEntity() {
		return User.builder().userid(userid).email(email).phone(phone).password(password).build();
	}
}
