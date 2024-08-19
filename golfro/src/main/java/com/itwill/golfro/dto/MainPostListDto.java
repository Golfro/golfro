package com.itwill.golfro.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.itwill.golfro.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MainPostListDto {
	private Long id;
	private String clubId;
	private String categoryId;
	private String title;
	private String userid;
	private Long views;
	private Long likes;
	private String createdTime;
	
	public static MainPostListDto fromEntity(Post entity) {
		return MainPostListDto.builder()
				.id(entity.getId())
				.clubId(entity.getClub().getId())
				.title(entity.getTitle())
				.userid(entity.getUser().getUserid())
				.views(entity.getViews())
				.likes(entity.getLikes())
				.createdTime(formatDateTime(entity.getCreatedTime()))
				.build();
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
