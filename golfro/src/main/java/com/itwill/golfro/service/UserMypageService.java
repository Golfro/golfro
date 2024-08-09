package com.itwill.golfro.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.dto.UserProfileDto;
import com.itwill.golfro.dto.UserUpdateDto;
import com.itwill.golfro.repository.Normal;
import com.itwill.golfro.repository.Pro;
import com.itwill.golfro.repository.UserMypage;
import com.itwill.golfro.repository.UserMypageDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMypageService {

	private final UserMypageDao userDao;

	public Normal read(String userid) {
		log.debug("read(userid={})", userid);

		Normal user = userDao.selectByUserid(userid);
		log.debug("select 결과 = {}", user);

		return user;
	}

	public Pro readPro(String userid) {
		log.debug("read(userid={})", userid);

		Pro pro = userDao.selectProByUserid(userid);
		log.debug("select 결과 = {}", pro);

		return pro;
	}
	
	// 닉네임 중복 체크: true - 중복되지 않은 닉네임(사용 가능한 닉네임), false - 중복된 닉네임
	public boolean checkNickname(String nickname) {
		log.debug("checkNickname(nickname={})", nickname);

		UserMypage user = userDao.selectByNickname(nickname);
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	public void update(UserUpdateDto user) {
		log.debug("update(user={})", user);

		userDao.update(user.toEntity());
	}
	
	public void updateImage(UserProfileDto user) {
		log.debug("updateImage(dto={})");

		userDao.updateImage(user.toEntity());
	}
	
	@Transactional
    public Object updateProfile(UserProfileDto user) {
		log.debug("updateProfile(user={})", user);
		
		if (user.getNickname() != null) {
			userDao.updateNickname(user.toEntity());
		}
        
        if (user.getGrade().equals("G10") && user.getCareer() != null) {
        	userDao.updateProCareer(user.toEntity());
        }
        
        if (user.getGrade().equals("G10")) {
        	return userDao.selectProByUserid(user.getUserid());
        }
        
        return userDao.selectByUserid(user.getUserid());
	}
	
}