package com.itwill.golfro.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.core.io.ByteArrayResource;

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
public class MyPostListDto {
	private Long id;
	private String title;
	private String userid;
	private String categoryId;
	private Long views;
	private Long likes;
	private LocalDateTime modifiedTime;
	private ByteArrayResource mediaResource; // 이미지 데이터 필드 추가

	public static MyPostListDto fromEntity(Post entity) {
		ByteArrayResource mediaResource = null;
		
		if (entity.getMedia() != null) {
			// 미디어 파일 경로를 바이트 배열로 변환하여 ByteArrayResource 생성
			byte[] mediaBytes = loadMediaBytes(entity.getMedia());
			mediaResource = new ByteArrayResource(mediaBytes);
		}

		return MyPostListDto
				.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.userid(entity.getUser().getUserid())
				.categoryId(entity.getCategory().getId())
				.views(entity.getViews())
				.likes(entity.getLikes())
				.modifiedTime(entity.getModifiedTime())
				.mediaResource(mediaResource).build();
	}

	// 미디어 파일 경로를 바이트 배열로 변환하는 메서드 (실제 구현 필요)
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
}
