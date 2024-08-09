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
public class exchangeInfoDto {
	private String userid;
	private String account;
	private Long withdraw;
	private Long point;

	public static exchangeInfoDto fromEntity(User entity) {
		return exchangeInfoDto.builder()
				.userid(entity.getUserid())
				.point(entity.getPoint())
				.account(entity.getAccount())
				.withdraw(entity.getWithdraw())
				.build();
	}
}
