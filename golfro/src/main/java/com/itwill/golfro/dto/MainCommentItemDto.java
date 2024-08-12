package com.itwill.golfro.dto;

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
	private String content;
	private Integer selection;

	public static MainCommentItemDto fromEntity(Comment entity) {
		return MainCommentItemDto.builder()
				.id(entity.getId())
				.postId(entity.getPost().getId())
				.userid(entity.getUser().getUserid())
				.content(entity.getContent())
				.selection(entity.getSelection())
				.build();
	}
}
