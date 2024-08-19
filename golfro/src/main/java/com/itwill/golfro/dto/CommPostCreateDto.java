package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class CommPostCreateDto {
	private String userid;
	private String title;
	private String content;
	private String categoryId;
	private MultipartFile media;
	private String mediaPath;
	
	public Post toEntity() {
		return Post.builder()
				.category(Category.builder().id(categoryId).build())
				.media(mediaPath)
				.user(User.builder().userid(userid).build())
				.title(title)
				.content(content)
				.build();
	}
}
