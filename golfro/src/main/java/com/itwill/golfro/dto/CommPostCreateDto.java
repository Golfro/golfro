package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Post;

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
				.categoryId(categoryId)
				.media(media)
				.userid(userid)
				.title(title)
				.content(content)
				.build();
	}
}
