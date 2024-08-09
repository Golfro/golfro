package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class expertUserCreateDto {
	private Integer id;
	private String password;
	private String nickname;
	private String email;
	private String address;
	private String accept;
	private String account;
	private String name;
	private Long birth;
	private String userid;
	private Integer gender;
	private Integer birthYear;
	private Integer birthMonth;
	private Integer birthDay;
	private String phone;
	private String phone0;
	private String phone1;
	private String phone2;
	private String phone3;
	private String postCode;
	private String addressMain;
	private String addressDetail;
	private String bank;
	private String accountNumber;
	private String emailPrefix;
	private String emailSeparator;

	public User toEntity() {
		birth = Long.parseLong(String.format("%04d%02d%02d", birthYear, birthMonth, birthDay));
		phone = phone0 + "/" + phone1 + "-" + phone2 + "-" + phone3;
		address = postCode + "/" + addressMain + "/" + addressDetail;
		email = emailPrefix + "@" + emailSeparator;
		account = bank + "/" + accountNumber;
		
		return User.builder().userid(userid).password(password).name(name).nickname(nickname).phone(phone).email(email)
				.birth(birth).gender(gender).address(address).account(account).accept(accept).build();
	}
}
