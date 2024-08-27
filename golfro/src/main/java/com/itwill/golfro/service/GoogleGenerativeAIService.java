package com.itwill.golfro.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
public class GoogleGenerativeAIService {

	public CompletableFuture<String> getResponse(String prompt) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				// Node.js 스크립트와 함께 프롬프트를 인자로 전달합니다.
				ProcessBuilder pb = new ProcessBuilder("node", "./src/main/resources/static/js/ai_chat.js", prompt);
				pb.redirectErrorStream(true);
				Process process = pb.start();

				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder output = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					output.append(line);
				}

				int exitCode = process.waitFor();
				if (exitCode != 0) {
					System.err.println("Node.js 스크립트 실행 중 오류 발생");
				}

				return output.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "AI 서비스 호출 중 오류 발생";
			}
		});
	}
}
