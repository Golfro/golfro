package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Data;

@Data
public class JoinPostCreateDto {
    private String title;
    private String content;
    private String userid;
    private User user;
    private String categoryId;
    private Category category;
    private String gcaddress;
    private LocalDateTime teeoff;
    private Long greenfee;
    private Integer hole;
    
    public Post toEntity() {
        return Post.builder()
        		.title(title)
        		.content(content)
        		.user(user)
        		.category(category)
        		.gcaddress(gcaddress)
        		.teeoff(teeoff)
        		.greenfee(greenfee)
        		.hole(hole)
        		.build();
    }
}
