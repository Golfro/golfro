package com.itwill.golfro.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itwill.golfro.domain.Post;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinPostListDto {
	private Long id;
	private String title;
	private String gcaddress;
	private LocalDateTime teeoff;
	private String formattedTeeoff;
	private Integer hole;
	private Long greenfee;
	private String userid;
	private String content;
	private Long views;
	private String categoryId;
	private LocalDateTime modifiedTime;

	public static JoinPostListDto fromEntity(Post entity) {
		String formattedTeeoff = formatDateTime(entity.getTeeoff());

		return JoinPostListDto.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.gcaddress(entity.getGcaddress())
				.teeoff(entity.getTeeoff())
				.formattedTeeoff(formattedTeeoff)
				.hole(entity.getHole())
				.greenfee(entity.getGreenfee())
				.userid(entity.getUser().getUserid())
				.content(entity.getContent())
				.views(entity.getViews())
				.categoryId(entity.getCategory().getId())
				.modifiedTime(entity.getModifiedTime())
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
