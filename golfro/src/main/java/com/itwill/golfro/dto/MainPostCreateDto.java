package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class MainPostCreateDto {
	private String clubId;
	private String userid;
	private String title;
	private String media;
	private String content;
	private String categoryId;
	private double height;
	private double career;
	private double handy;
	private double ironDistance;
	private double driverDistance;
	
	public Post toEntity() {
		return Post.builder()
				.club(Club.builder().id(clubId).build())
				.category(Category.builder().id(categoryId).build())
				.media(media)
				.user(User.builder().userid(userid).build())
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
