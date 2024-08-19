package com.itwill.golfro.dto;

import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
public class MainPostUpdateDto {
	private Long id;
	private String clubId;
	private String title;
	private String content;
	private Integer height;
	private Integer career;
	private Integer handy;
	private Integer irondistance;
	private Integer driverdistance;
	private MultipartFile media;
	private String mediaPath;
	
	public Post toEntity() {
	    return Post.builder()
	            .id(id)
	            .club(Club.builder().id(clubId).build())
	            .title(title)
	            .content(content)
	            .height(height)
	            .career(career)
	            .handy(handy)
	            .irondistance(irondistance)
	            .driverdistance(driverdistance)
	            .build();
	}
}
