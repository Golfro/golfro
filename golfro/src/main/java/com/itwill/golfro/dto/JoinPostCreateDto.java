package com.itwill.golfro.dto;

import java.time.LocalDateTime;

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
}
