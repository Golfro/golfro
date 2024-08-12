package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Post;

import lombok.Data;

@Data
public class JoinPostCreateDto {
    private String title;
    private String content;
    private String userid;
    private String categoryId;
    private String gcaddress;
    private LocalDateTime teeoff;
    private Long greenfee;
    private Integer hole;
    
    public Post toEntity() {
        return Post.builder()
        		.title(title)
        		.content(content)
        		.userid(userid)
        		.categoryId(categoryId)
        		.gcaddress(gcaddress)
        		.teeoff(teeoff)
        		.greenfee(greenfee)
        		.hole(hole)
        		.build();
    }
}
