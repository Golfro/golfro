package com.itwill.golfro.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.gaebokchi.repository.ReviewPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
// 새 글 작성 요청에서 요청 파라미터들을 저장하기 위한 DTO
public class ReviewPostCreateDto {
	// 필드 이름을 요청 파라미터 이름과 같게 선언 & 기본 생성자 & setter.
	private String title;
	private String content;
	private String author;
	private String category;
	private String clubtype;
	private MultipartFile media;
	private String mediaPath;

	public ReviewPost toEntity() {
		log.debug("postToEntity()");
		return ReviewPost.builder().title(title).content(content).author(author).media(mediaPath).clubtype(clubtype)
				.category(category).build();
	}

}
