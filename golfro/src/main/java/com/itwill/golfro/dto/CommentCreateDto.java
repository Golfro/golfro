package com.itwill.golfro.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long postId;
    private String content;
    private String userid;
}
