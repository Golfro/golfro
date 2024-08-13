package com.itwill.golfro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.UserProfileDto;
import com.itwill.golfro.dto.UserUpdateDto;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMypageService {

	private final UserRepository userRepo;

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
	public void update(UserUpdateDto dto) {
		log.info("update(dto={})", dto);

		User entity = userRepo.findByUserid(dto.getUserid());
		
		entity.update(dto.getPassword(), dto.getPhone(), dto.getAddress()
				, dto.getProId(), dto.getAccount());
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
        	entity.updateProCareer(dto.getCareer());
        }
        
        if (dto.getGrade().equals("G10")) {
        	return userRepo.selectProByUserid(dto.getUserid());
        }
        
        return userRepo.selectByUserid(dto.getUserid());
	}
	
}