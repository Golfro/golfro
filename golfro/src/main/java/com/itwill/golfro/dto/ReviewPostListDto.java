package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewPostListDto {
	private Long id;
	private String title;
	private String userid;
	private String categoryId;
	private Long views;
	private Long likes;
	private LocalDateTime createdTime;
	private String media;

	public static ReviewPostListDto fromEntity(Post entity) {
		return ReviewPostListDto.builder()
				.id(entity.getId())
				.categoryId(entity.getCategory().getId())
				.title(entity.getTitle())
				.userid(entity.getUser().getUserid())
				.createdTime(entity.getCreatedTime())
				.views(entity.getViews())
				.likes(entity.getLikes())
				.media(entity.getMedia()) // 이미지 데이터 설정
				.build();
	}
}
