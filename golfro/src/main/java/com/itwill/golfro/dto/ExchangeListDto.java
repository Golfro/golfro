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
public class ExchangeListDto {
	private String userid;
	private String name;
	private Long withdraw;

	public static ExchangeListDto fromEntity(User entity) {
		return ExchangeListDto.builder()
				.userid(entity.getUserid())
				.name(entity.getName())
				.withdraw(entity.getWithdraw())
				.build();
	}
}
