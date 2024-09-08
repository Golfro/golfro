package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
// 새 글 작성 요청에서 요청 파라미터들을 저장하기 위한 DTO
public class ReviewPostCreateDto {
	// 필드 이름을 요청 파라미터 이름과 같게 선언 & 기본 생성자 & setter.
	private String title;
	private String content;
	private String userid;
	private User user;
	private String categoryId;
	private Category category;
	private MultipartFile media;
	private String mediaPath;

	public Post toEntity() {
		return Post.builder()
				.title(title)
				.content(content)
				.user(user)
				.category(Category.builder().id(categoryId).build())
				.media(mediaPath)
				.build();
	}
}
