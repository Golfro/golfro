package com.itwill.golfro.dto;

import com.itwill.gaebokchi.repository.UserMypage;

import lombok.Data;

@Data
public class UserProfileDto {
	private String userid;
	private String image;
	private String nickname;
	private String grade;
	private String career;
	
	public UserMypage toEntity() {
		return UserMypage.builder()
				.image(image)
				.userid(userid)
				.nickname(nickname)
				.career(career)
				.build(); 
	}
}
