package com.itwill.golfro.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.gaebokchi.repository.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Component
public class MainPostCreateDto {
	// 파라미터 이름과 동일하게 작
	
	private String clubType;
	private String author;
	private String title;
	private MultipartFile media;
	private String content;
	private String category;
	private Integer height;
	private Integer career;
	private Integer handy;
	private Integer ironDistance;
	private Integer driverDistance;
	
	private String mediaPath;
	

	
	public Post toEntity() {
		log.debug("postToEntity()");
		return Post.builder().clubType(clubType).category(category).media(mediaPath).author(author).title(title).content(content).height(height).career(career).handy(handy).ironDistance(ironDistance).driverDistance(driverDistance).build();
	}
	
	
	

}
