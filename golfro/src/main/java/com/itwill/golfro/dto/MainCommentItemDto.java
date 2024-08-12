package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Comment;

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
	private Long id;
	private Long postId;
	private String userid;
	private String nickname;
	private String image;
	private String content;
	private Integer selection;
	private LocalDateTime modifiedTime;
	
	public static MainCommentItemDto fromEntity(Comment entity) {
		return MainCommentItemDto.builder()
				.id(entity.getId())
				.postId(entity.getPost().getId())
				.userid(entity.getUser().getUserid())
				.nickname(entity.getUser().getNickname())
				.image(entity.getUser().getImage())
				.content(entity.getContent())
				.selection(entity.getSelection())
				.modifiedTime(entity.getModifiedTime())
				.build();
	}
}
