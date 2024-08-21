package com.itwill.golfro.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest chatRequest) {
        String userMessage = chatRequest.getMessage();
        
        // 응답을 결정하는 간단한 로직
        String response = getResponse(userMessage);

        return ResponseEntity.ok(response);
    }

    private String getResponse(String message) {
        // 간단한 응답 로직
        switch (message) {
            case "안녕":
                return "안녕하세요! 어떻게 도와드릴까요?";
            case "날씨":
                return "오늘의 날씨는 맑고 따뜻합니다.";
            case "이름":
                return "저는 당신의 친절한 AI 어시스턴트입니다.";
            default:
                return "저는 다양한 질문에 답할 수 있습니다. 어떤 도움이 필요하신가요?";
        }
    }

    public static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    
}
