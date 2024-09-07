package com.itwill.golfro.dto;

import org.springframework.core.io.ByteArrayResource;

import com.itwill.golfro.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommPostListDto {
	private Long id;
	private String title;
	private String userid;
	private String categoryId;
	private Long views;
	private Long likes;
	private ByteArrayResource mediaResource; // 이미지 데이터 필드 추가
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	private String formattedCreatedTime; // 포맷팅된 날짜를 저장할 필드 추가
	
	public static CommPostListDto fromEntity(Post entity) {
		ByteArrayResource mediaResource = null;
		if (entity.getMedia() != null) {
			// 이미지 파일 경로를 바이트 배열로 변환하여 ByteArrayResource 생성
			byte[] mediaBytes = loadMediaBytes(entity.getMedia());
			mediaResource = new ByteArrayResource(mediaBytes);
		}

		LocalDateTime createdTime = entity.getCreatedTime();
		String formattedCreatedTime = formatDateTime(createdTime);
		
		return CommPostListDto.builder()
				.id(entity.getId())
				.categoryId(entity.getCategory().getId())
				.title(entity.getTitle())
				.userid(entity.getUser().getUserid())
				.createdTime(createdTime)
				.formattedCreatedTime(formattedCreatedTime)
				.views(entity.getViews())
				.likes(entity.getLikes())
				.mediaResource(mediaResource) // 이미지 데이터 설정
				.build();
	}

	// 이미지 파일 경로를 바이트 배열로 변환하는 메서드 (실제 구현 필요)
	private static byte[] loadMediaBytes(String mediaPath) {
	    try {
	        URL url = new URL(mediaPath); // URL 객체 생성
	        try (InputStream inputStream = url.openStream(); // URL에서 InputStream을 열고
	             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) { // 바이트 배열로 변환할 OutputStream 생성
	             
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, length);
	            }
	            return outputStream.toByteArray(); // 바이트 배열 반환
	        }
	    } catch (IOException e) {
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