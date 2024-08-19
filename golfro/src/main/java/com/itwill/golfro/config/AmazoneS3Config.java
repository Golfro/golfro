//package com.itwill.golfro.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//
//@Configuration
//public class AmazoneS3Config {
//	
//	
//    @Bean
//    public AmazonS3 amazonS3() {
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
//                "AKIARSU7K5TGM54L5VWS", 
//                "p0t+0qhZzAvaRj2qAH//f3tE3L41LH4NDLJml0Y9"
//        );
//
//        return AmazonS3ClientBuilder.standard()
//                .withRegion("ap-northeast-2")
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .build();
//    }
//
//}
