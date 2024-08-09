package com.itwill.golfro.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.itwill.gaebokchi.repository.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MainPostListDto {
	
	private Integer id;
	private String clubType;
	private String title;
	private String author;
	private Integer views;
	private Integer likes;
	private String createdTime;
	private String selection;
	private LocalDateTime modifiedTime;
	private String category;
	
	
	public static MainPostListDto fromEntity(Post post) {
		
		return MainPostListDto.builder().id(post.getId()).clubType(post.getClubType()).title(post.getTitle()).author(post.getAuthor()).selection(post.getSelection())
				.views(post.getViews()).likes(post.getLikes()).createdTime(formatDateTime(post.getCreatedTime())).build();
				
	}
	
    private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            // UTC 시간대로 변환
            ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("UTC"));
            // 한국 시간대로 변환
            ZonedDateTime seoulDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
            // 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 포맷 적용
            return seoulDateTime.format(formatter);
        } else {
            return null;
        }
    }
	

}
