package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
// 업데이트 요청의 요청 파라미터들을 저장하기 위한 DTO
public class CommPostUpdateDto {
	private Long id;
	private String title;
	private String content;
	private String categoryId;
	private String media;

	public Post toEntity() {
		return Post.builder()
				.id(id)
				.title(title)
				.content(content)
				.media(media)
				.category(Category.builder().id(categoryId).build())
				.build();
	}
}
