package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
// 업데이트 요청의 요청 파라미터들을 저장하기 위한 DTO
public class JoinPostUpdateDto {
		private Long id;
	  	private String title;
	    private String content;
	    private String clubId;
	    private String gcaddress;
	    private LocalDateTime teeoff;
	    private Long greenfee;
	    private Integer hole;

	    public Post toEntity() {
	        return Post.builder()
	        		.id(id)
	        		.title(title)
	        		.content(content)
	        		.clubId(clubId)
	        		.gcaddress(gcaddress)
	        		.teeoff(teeoff)
	        		.greenfee(greenfee)
	        		.hole(hole)
	        		.build();
	    }
	    
}
