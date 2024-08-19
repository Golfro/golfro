package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class UpdatePointDto {
	private String userid;
	private String password;
	private Long withdraw;
	private Long point;
	
	public User toAcceptEntity() {
		return User.builder()
				.userid(userid)
				.withdraw(withdraw)
				.point(point - withdraw)
				.build(); 
	}
	
	public User toRejectEntity() {
		return User.builder()
				.userid(userid)
				.withdraw(0L)
				.point(point)
				.build(); 
	}
}
