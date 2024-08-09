package com.itwill.golfro.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ByteArrayResource;

import com.itwill.gaebokchi.repository.ReviewPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPostListDto {
	private Integer id;
	private String title;
	private String author;
	private String category;
	private LocalDateTime createdTime; // 변경된 필드 타입
	private Integer views;
	private Integer likes;
	private ByteArrayResource mediaResource;

	// 포맷팅된 날짜를 저장할 필드 추가
	private String formattedCreatedTime;

	public static ReviewPostListDto fromEntity(ReviewPost reviewpost) {
		ByteArrayResource mediaResource = null;
		if (reviewpost.getMediaPath() != null) {
			// 이미지 파일 경로를 바이트 배열로 변환하여 ByteArrayResource 생성
			byte[] mediaBytes = loadMediaBytes(reviewpost.getMediaPath());
			mediaResource = new ByteArrayResource(mediaBytes);
		}

		LocalDateTime createdTime = reviewpost.getCreatedTime();
		String formattedCreatedTime = formatDateTime(createdTime);

		return ReviewPostListDto.builder().id(reviewpost.getId()).category(reviewpost.getCategory())
				.title(reviewpost.getTitle()).author(reviewpost.getAuthor()).createdTime(createdTime)
				.formattedCreatedTime(formattedCreatedTime) // 포맷팅된 날짜 설정
				.views(reviewpost.getViews()).likes(reviewpost.getLikes()).mediaResource(mediaResource) // 이미지 데이터 설정
				.build();
	}

	// 이미지 파일 경로를 바이트 배열로 변환하는 메서드 (실제 구현 필요)
	private static byte[] loadMediaBytes(String mediaPath) {
		try {
			Path path = Paths.get(mediaPath);
			return Files.readAllBytes(path);
		} catch (IOException e) {
			// 예외 처리: 파일을 읽을 수 없는 경우 등
			e.printStackTrace();
			return new byte[0]; // 빈 바이트 배열 리턴
		}
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
