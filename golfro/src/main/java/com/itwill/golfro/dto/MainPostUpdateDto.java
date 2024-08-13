package com.itwill.golfro.dto;

import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
public class MainPostUpdateDto {
	private Long id;
	private String clubId;
	private String title;
	private String content;
	private String media;
	private Integer height;
	private Integer career;
	private Integer handy;
	private Integer irondistance;
	private Integer driverdistance;
	
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
