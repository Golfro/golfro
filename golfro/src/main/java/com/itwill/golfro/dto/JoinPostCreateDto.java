package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;

import lombok.Builder;
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
        		.user(User.builder().userid(userid).build())
        		.category(Category.builder().id(categoryId).build())
        		.gcaddress(gcaddress)
        		.teeoff(teeoff)
        		.greenfee(greenfee)
        		.hole(hole)
        		.build();
    }
}
