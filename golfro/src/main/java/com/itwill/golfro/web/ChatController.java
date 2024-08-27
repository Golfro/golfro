package com.itwill.golfro.web;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.golfro.service.GoogleGenerativeAIService;

@RestController
public class ChatController {

    private final GoogleGenerativeAIService aiService;

    public ChatController(GoogleGenerativeAIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public CompletableFuture<ResponseEntity<String>> chat(@RequestBody ChatRequest chatRequest) {
        String userMessage = chatRequest.getMessage();
        
        return aiService.getResponse(userMessage)
                .thenApply(response -> ResponseEntity.ok(response));
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
