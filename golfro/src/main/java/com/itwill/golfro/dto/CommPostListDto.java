package com.itwill.golfro.dto;

import org.springframework.core.io.ByteArrayResource;

import com.itwill.golfro.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

	public static CommPostListDto fromEntity(Post entity) {
		ByteArrayResource mediaResource = null;
		if (entity.getMedia() != null) {
			// 이미지 파일 경로를 바이트 배열로 변환하여 ByteArrayResource 생성
			byte[] mediaBytes = loadMediaBytes(entity.getMedia());
			mediaResource = new ByteArrayResource(mediaBytes);
		}

		return CommPostListDto.builder()
				.id(entity.getId())
				.categoryId(entity.getCategoryId())
				.title(entity.getTitle())
				.userid(entity.getUserid())
				.views(entity.getViews())
				.likes(entity.getLikes())
				.mediaResource(mediaResource) // 이미지 데이터 설정
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
}