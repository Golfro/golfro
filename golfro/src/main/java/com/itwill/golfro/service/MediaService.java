package com.itwill.golfro.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaService {
	private final String mediaDirectory = "C:\\Users\\itwill\\Desktop\\media";

	public String storeFile(MultipartFile file) {
		try {
			Path uploadPath = Paths.get(mediaDirectory);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			String originalFileName = file.getOriginalFilename();
			String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
			String fileName = UUID.randomUUID().toString() + fileExtension;
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(file.getInputStream(), filePath);
			log.info("Stored file at: {}", filePath.toString());
			return fileName;
		} catch (IOException e) {
			log.error("Error storing file", e);
			throw new RuntimeException("Failed to store file", e);
		}
	}

	public ByteArrayResource loadFileAsResource(String fileName) {
		try {
			Path filePath = Paths.get(mediaDirectory).resolve(fileName).normalize();
			byte[] data = Files.readAllBytes(filePath);
			return new ByteArrayResource(data);
		} catch (IOException e) {
			log.error("Error loading file", e);
			throw new RuntimeException("Failed to load file", e);
		}
	}

	public String getMediaUrl(String mediaFileName) {
		String mediaUrl = "/media/" + mediaFileName; // 실제 서버의 경로와 맞추기
		log.info("Generated media URL: {}", mediaUrl);
		return mediaUrl;
	}
}
