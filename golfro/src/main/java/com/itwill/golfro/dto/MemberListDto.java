package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberListDto {
	private String userid;
	private String name;
	private String grade;
	
	public static MemberListDto fromEntity(User entity) {
		return MemberListDto.builder()
				.userid(entity.getUserid())
				.name(entity.getName())
				.grade(entity.getGrade().getId())
				.build();
	}
}
