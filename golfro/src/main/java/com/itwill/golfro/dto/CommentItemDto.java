package com.itwill.golfro.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itwill.golfro.domain.Comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentItemDto {
	private Long id;
	private String content;
	private String userid;
	private LocalDateTime modifiedTime;
	private String formattedModifiedTime;
	
	// Comment 타입의 객체를 CommentItemDto 타입 객체로 변환해서 리턴하는 메서드.
	public static CommentItemDto fromEntity(Comment entity) {
		// LocalDateTime을 포맷팅하여 문자열로 변환
		String formattedModifiedTime = formatDateTime(entity.getModifiedTime());
		
		return CommentItemDto.builder()
				.id(entity.getId())
				.content(entity.getContent())
				.userid(entity.getUser().getUserid())
				.modifiedTime(entity.getModifiedTime())
				.formattedModifiedTime(formattedModifiedTime)
				.build();
	}
	
	// LocalDateTime을 포맷팅하여 문자열로 변환하는 메서드
	private static String formatDateTime(LocalDateTime dateTime) {
		if (dateTime != null) {
			// 포맷 정의
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			// 포맷 적용
			return dateTime.format(formatter);
		} else {
			return null;
		}
	}
}
