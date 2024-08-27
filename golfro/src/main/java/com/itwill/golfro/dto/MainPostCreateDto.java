package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class MainPostCreateDto {
	private String clubId;
	private User user;
	private String userid;
	private String title;
	private String content;
	private String categoryId;
	private double height;
	private double career;
	private double handy;
	private double ironDistance;
	private double driverDistance;
	private MultipartFile media;
	private String mediaPath;
	
	public Post toEntity() {
		return Post.builder()
				.club(Club.builder().id(clubId).build())
				.category(Category.builder().id(categoryId).build())
				.media(mediaPath)
				.user(user)
				.title(title)
				.content(content)
				.height(height)
				.career(career)
				.handy(handy)
				.irondistance(ironDistance)
				.driverdistance(driverDistance)
				.build();
	}
}
