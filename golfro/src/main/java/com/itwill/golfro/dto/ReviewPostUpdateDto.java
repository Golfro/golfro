package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
// 업데이트 요청의 요청 파라미터들을 저장하기 위한 DTO
public class ReviewPostUpdateDto {
    private Long id;
    private String title;
    private String content;
    
    public Post toEntity() {
        return Post.builder()
        		.id(id)
        		.title(title)
        		.content(content)
        		.build();
    }
}
