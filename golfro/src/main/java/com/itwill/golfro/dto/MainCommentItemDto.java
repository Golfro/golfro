package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MainCommentItemDto {
    private Long id; // Comment PK번호
    private String nickName; // User의 닉네임
    private String image; // User의 프로필 사진
    private String content; // 댓글 내용
    private LocalDateTime createdTime; // 댓글 최초 작성 시간
    private LocalDateTime modifiedTime; // 댓글 최종 수정 시간
    private Integer selection; // 선택 여부(0 또는 1)
    private Long postId; // post PK번호
    
    
    
    

    
	
//	private Long id;
//	private Long postId;
//	private User user;
//	private String image;
//	private String content;
//	private Integer selection;
//	private LocalDateTime modifiedTime;
//	
//	public static MainCommentItemDto fromEntity(Comment entity) {
//		return MainCommentItemDto.builder()
//				.id(entity.getId())
//				.postId(entity.getPost().getId())
//				.user(entity.getUser())
//				.image(entity.getUser().getImage())
//				.content(entity.getContent())
//				.selection(entity.getSelection())
//				.modifiedTime(entity.getModifiedTime())
//				.build();
//	}
    
    
}
