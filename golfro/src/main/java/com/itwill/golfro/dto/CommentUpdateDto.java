package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Comment;

import lombok.Data;

// 댓글 업데이트 요청의 요청 파라미터들을 저장하기 위한 DTO.
@Data
public class CommentUpdateDto {
    private Long id;
    private String content;

    // CommentUpdateDto 타입을 Commnet 타입으로 변환해서 리턴.
    public Comment toEntity() {
        return Comment.builder()
        		.id(id)
        		.content(content)
        		.build();
    }
}
