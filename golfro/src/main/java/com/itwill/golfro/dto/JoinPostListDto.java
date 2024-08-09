package com.itwill.golfro.dto;

import com.itwill.gaebokchi.repository.JoinPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinPostListDto {
	private Integer id;
	private String title;
	private String gcadress;
	private LocalDateTime teeoff;
	private Integer hole;
	private Integer greenfee;
	private String author;
	private String content;
	private Integer views;
	private LocalDateTime modifiedTime;
	private String category;

	private String formattedTeeoff;

	public static JoinPostListDto fromEntity(JoinPost joinpost) {
		String formattedTeeoff = formatDateTime(joinpost.getTeeoff());

		return JoinPostListDto.builder().id(joinpost.getId()).title(joinpost.getTitle())
				.gcadress(joinpost.getGcadress()).teeoff(joinpost.getTeeoff()).formattedTeeoff(formattedTeeoff)
				.hole(joinpost.getHole()).greenfee(joinpost.getGreenfee()).author(joinpost.getAuthor())
				.content(joinpost.getContent()).views(joinpost.getViews()).modifiedTime(joinpost.getModifiedTime())
				.category(joinpost.getCategory()).build();
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
