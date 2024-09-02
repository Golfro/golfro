package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class MainPostUpdateDto {
	private Long id;
	private String categoryId;
	private String userid;
	private User user;
	private String clubId;
	private String title;
	private String content;
	private Integer height;
	private Integer career;
	private Integer handy;
	private Integer ironDistance;
	private Integer driverDistance;
	
	public Post toEntity() {
	    return Post.builder()
	            .id(id)
	            .user(user)
	            .club(Club.builder().id(clubId).build())
	            .category(Category.builder().id(categoryId).build())
	            .title(title)
	            .content(content)
	            .height(height)
	            .career(career)
	            .handy(handy)
	            .ironDistance(ironDistance)
	            .driverDistance(driverDistance)
	            .build();
	}
}
