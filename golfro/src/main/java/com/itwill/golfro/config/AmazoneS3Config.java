package com.itwill.golfro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazoneS3Config {
	
	@Bean
	public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion("YOUR_AWS_REGION") // 예: us-east-1
                .build();
    }
	

}
