package com.itwill.golfro.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.core.io.ByteArrayResource;

import com.itwill.gaebokchi.repository.MyPost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO(Data Transfer Object)
// 뷰 <--> 컨트롤러, 컨트롤러 <--> 서비스 사이에서 데이터를 주고 받을 때 사용하는 객체.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostListDto {
	private Integer id;
	private String title;
	private String author;	
	private String category;
	private LocalDateTime modifiedTime;
	private Integer views;
	private Integer likes;
	private ByteArrayResource mediaResource; // 이미지 데이터 필드 추가

	public static MyPostListDto fromEntity(MyPost mypost) {
		ByteArrayResource mediaResource = null;
		if (mypost.getMediaPath() != null) {
			// 이미지 파일 경로를 바이트 배열로 변환하여 ByteArrayResource 생성
			byte[] mediaBytes = loadMediaBytes(mypost.getMediaPath());
			mediaResource = new ByteArrayResource(mediaBytes);
		}

		return MyPostListDto
				.builder()
				.id(mypost.getId())
				.title(mypost.getTitle())
				.author(mypost.getAuthor())
				.modifiedTime(mypost.getModifiedTime())
				.views(mypost.getViews())
				.likes(mypost.getLikes())
				.category(mypost.getCategory())
				.mediaResource(mediaResource).build();
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
