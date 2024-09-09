package com.itwill.golfro.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.UserProfileDto;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMypageService {

	private final UserRepository userRepo;

	private final AmazonS3 amazonS3;
	private final String bucketName = "golfro-bucket";
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public User read(String userid) {
		log.info("read(userid={})", userid);

		User user = userRepo.selectByUserid(userid);
		log.info("select 결과={}", user);

		return user;
	}

	public User readPro(String userid) {
		log.info("readPro(userid={})", userid);

		User user = userRepo.selectProByUserid(userid);
		log.info("select 결과={}", user);

		return user;
	}
	
	@Transactional(readOnly = true)
	// 닉네임 중복 체크: true - 중복되지 않은 닉네임(사용 가능한 닉네임), false - 중복된 닉네임
	public boolean checkNickname(String nickname) {
		log.info("checkNickname(nickname={})", nickname);

		User user = userRepo.selectByNickname(nickname);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public void update(User user) {
		log.info("update(user={})", user);

		User entity = userRepo.findByUserid(user.getUserid());
		
		entity.update(passwordEncoder.encode(user.getPassword()), user.getPhone(), user.getAddress(), user.getAccount());
	}
	
	@Transactional
	public void updateImage(UserProfileDto dto) {
		log.info("updateImage(dto={})", dto);

		User entity = userRepo.findByUserid(dto.getUserid());
		
		entity.updateImage(dto.getImage());
	}
	
	@Transactional
    public Object updateProfile(UserProfileDto dto) {
		log.info("updateProfile(dto={})", dto);
		
		User entity = userRepo.findByUserid(dto.getUserid());
		
		if (dto.getNickname() != null) {
			entity.updateNickname(dto.getNickname());
		}
        
        if (dto.getGrade().equals("G10") && dto.getCareer() != null) {
        	entity.getPro().updateProCareer(dto.getCareer());
        }
        
        if (dto.getGrade().equals("G10")) {
        	return userRepo.selectProByUserid(dto.getUserid());
        }
        
        return userRepo.selectByUserid(dto.getUserid());
	}
	
	@Transactional
	public void uploadImage(String userid, MultipartFile mediaFile) {
		// 파일이 비어있는지 체크
		if (mediaFile.isEmpty()) {
			log.info("Please select a file to upload.");
		}

		String mediaName = mediaFile.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고
        mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제
        int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서
		
		if (idx > 0) { // idx가 유효한지 확인
            String orgMediaName = mediaName.substring(0, idx); // 파일 이름의 첫 번째 인덱스부터 . 인덱스 전까지 저장
            String orgMediaType = mediaName.substring(idx); // 파일 이름의 . 다음 인덱스부터 확장자까지 저장
            String s3FileName = orgMediaName + orgMediaType; // 파일 순수 이름 + 랜덤 UUID + 확장자를 합쳐서 저장
        
			try {
				// ObjectMetadata를 사용하여 파일의 Content-Length 설정
		        ObjectMetadata metadata = new ObjectMetadata();
		        metadata.setContentLength(mediaFile.getSize());
		        metadata.setContentType(mediaFile.getContentType()); // 파일의 Content-Type을 설정
				
				// 파일을 S3에 업로드
		        amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, mediaFile.getInputStream(), metadata));
				
		        // S3에 저장된 파일의 URL을 생성
		        String mediaPath = amazonS3.getUrl(bucketName, s3FileName).toString();
		
		        UserProfileDto dto = new UserProfileDto();
				dto.setUserid(userid);
				dto.setImage(mediaPath);

				updateImage(dto);
		
				// 업로드 성공 메시지 전달
				log.info("File uploaded successfully: {}", mediaFile.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
				// 업로드 실패 메시지 전달
				log.info("Failed to upload file: {}", mediaFile.getOriginalFilename());
			} 
        } else {
        	log.error("Invalid media file name: {}", mediaName);
        	throw new IllegalArgumentException("Invalid media file name: " + mediaName);
        }
	}
	
}