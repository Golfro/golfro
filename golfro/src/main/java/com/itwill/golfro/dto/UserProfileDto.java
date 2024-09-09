package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Pro;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class UserProfileDto {
	private String userid;
	private String image;
	private String nickname;
	private String grade;
	private Pro pro;
	private String career;
	
	public User toEntity() {
		return User.builder()
				.image(image)
				.userid(userid)
				.nickname(nickname)
				.pro(pro.updateProCareer(career))
				.build(); 
	}
}
