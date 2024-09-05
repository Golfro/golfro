package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class CommentCreateDto {
	private Long id;
    private Post post;
    private String content;
    private String userid;
    private User user;
    
    public Comment toEntity() {
        return Comment.builder()
                .post(post)
                .content(content)
                .user(user)
                .build();
    }
}
