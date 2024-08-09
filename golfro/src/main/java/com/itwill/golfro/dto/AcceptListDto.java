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
public class AcceptListDto {
	private String userid;
	private String name;
	private String email;
	private String accept;
	
	public static AcceptListDto fromEntity(User entity) {
		return AcceptListDto.builder()
				.userid(entity.getUserid())
				.name(entity.getName())
				.email(entity.getEmail())
				.accept(entity.getAccept())
				.build();
	}
}
