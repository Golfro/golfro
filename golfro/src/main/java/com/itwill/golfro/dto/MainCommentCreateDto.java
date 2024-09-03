package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class MainCommentCreateDto {
	private Long postId;
	private String content;
	private Long userid;
	
	public Comment toEntity() {
		return Comment.builder()
				.post(Post.builder().id(postId).build())
				.content(content)
				.user(User.builder().id(userid).build())
				.build();
	}
}
