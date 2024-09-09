package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class UserUpdateDto {
	private String userid;
	private String password;
	private String changePassword;
	private String carrier;
	private String number;
	private String phone;
	private String postcode;
	private String mainAddress;
	private String detailAddress;
	private String address;
	private String grade;
	private String bank;
	private String bank_account;
	private String account;

	public User toEntity() {
		address = postcode + "/" + mainAddress + "/" + detailAddress;
		phone = carrier + "/" + number;
		account = bank + "/" + bank_account;

		if (!changePassword.equals("")) {
			return User
					.builder()
					.userid(userid)
					.password(changePassword)
					.phone(phone)
					.address(address)
					.account(account)
					.build();
		}

		return User
				.builder()
				.userid(userid)
				.password(password)
				.phone(phone)
				.address(address)
				.account(account)
				.build();
	}
}
