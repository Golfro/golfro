package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class CommPostCreateDto {
	private String userid;
	private String title;
	private String media;
	private String content;
	private String categoryId;
	
	public Post toEntity() {
		return Post.builder()
				.category(Category.builder().id(categoryId).build())
				.media(media)
				.user(User.builder().userid(userid).build())
				.title(title)
				.content(content)
				.build();
	}
}
