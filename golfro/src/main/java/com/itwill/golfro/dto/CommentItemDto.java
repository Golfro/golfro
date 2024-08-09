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
public class CommentItemDto {
	private Long id;
	private String content;
	private String userid;

	public static CommentItemDto fromEntity(Comment entity) {
		return CommentItemDto.builder()
				.id(entity.getId())
				.content(entity.getContent())
				.userid(entity.getUser().getUserid())
				.build();
	}
}
