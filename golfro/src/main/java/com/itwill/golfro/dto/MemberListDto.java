package com.itwill.golfro.dto;

import com.itwill.gaebokchi.repository.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberListDto {
	private String userid;
	private String name;
	private String grade;
	
	public static MemberListDto fromEntity(User user) {
		return MemberListDto.builder().userid(user.getUserid()).name(user.getName()).grade(user.getGrade()).build();
	}
	
}
