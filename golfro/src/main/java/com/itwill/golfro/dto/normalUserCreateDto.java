package com.itwill.golfro.dto;

import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class normalUserCreateDto {
	private Long id;
	private String userid;
	private String password;
	private String name;
	private String nickname;
	private String phone;
	private String email;
	private Long birth;
	private Integer gender;
	private String address;
	private int birthYear;
	private int birthMonth;
	private int birthDay;
	private String phone0;
	private String phone1;
	private String phone2;
	private String phone3;
	private String postCode;
	private String addressMain;
	private String addressDetail;
	private String emailPrefix;
	private String emailSeparator;

	public User toEntity() {
		birth = Long.parseLong(String.format("%04d%02d%02d", birthYear, birthMonth, birthDay));
		phone = phone0 + "/" + phone1 + "-" + phone2 + "-" + phone3;
		address = postCode + "/" + addressMain + "/" + addressDetail;
		email = emailPrefix + "@" + emailSeparator;

		return User.builder()
				.userid(userid)
				.password(password)
				.name(name)
				.nickname(nickname)
				.phone(phone)
				.email(email)
				.birth(birth)
				.gender(gender)
				.address(address)
				.build();
	}
}
