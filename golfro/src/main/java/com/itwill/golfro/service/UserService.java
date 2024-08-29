package com.itwill.golfro.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.AcceptListDto;
import com.itwill.golfro.dto.ExchangeListDto;
import com.itwill.golfro.dto.MemberListDto;
import com.itwill.golfro.dto.UpdatePasswordDto;
import com.itwill.golfro.dto.UpdatePointDto;
import com.itwill.golfro.dto.UserSignInDto;
import com.itwill.golfro.dto.exchangeInfoDto;
import com.itwill.golfro.dto.expertUserCreateDto;
import com.itwill.golfro.dto.findIdDto;
import com.itwill.golfro.dto.findPasswordDto;
import com.itwill.golfro.dto.normalUserCreateDto;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;

	@Transactional(readOnly = true)
	public boolean checkUserid(String userid) {
		log.info("checkUserid(userid={})", userid);
		
		User user = userRepo.selectByUserid(userid);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean checkNickname(String nickname) {
		log.info("checkNickname(nickname={})", nickname);
		
		User user = userRepo.selectByNickname(nickname);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional(readOnly = true)
	public boolean checkEmail(String email) {
		log.info("checkEmail(email={})", email);
		
		User user = userRepo.selectByEmail(email);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}

	}

	@Transactional(readOnly = true)
	public boolean checkPhone(String phone) {
		log.info("checkPhone(phone={})", phone);
		
		User user = userRepo.selectByPhone(phone);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean checkAccept(String accept) {
		log.info("checkAccept(accept={})", accept);
		
		User user = userRepo.selectByAccept(accept);
		
		if (user == null) {
			return true;
		} else {
			return false;
		}
	}

//	public void nomalUserCreate(normalUserCreateDto dto) {
//		log.info("nomalUserCreate(dto={})", dto);
//		
//		userRepo.save(dto.toEntity());
//	}
//
//	public void expertUserCreate(expertUserCreateDto dto) throws Exception {
//		log.info("expertUserCreate(dto={})", dto);
//		
//		try {
//			userRepo.save(dto.toEntity());
//		} catch (Exception e) {
//			throw new Exception("Failed to insert into pros table", e);
//		}
//	}

//	@Transactional(readOnly = true)
//	public User read(UserSignInDto dto) {
//		log.info("UserSignInDto(dto={})", dto);
//		
//		User user = userRepo.selectByUseridAndPassword(dto.getUserid(), dto.getPassword());
//
//		return user;
//	}

	@Transactional(readOnly = true)
	public User findUserid(findIdDto dto) {
		log.info("findUserid(dto={})", dto);
		
		User user = userRepo.FindUserid(dto.getName(), dto.getEmail());
		
		return user;
	}

	@Transactional(readOnly = true)
	public boolean findPassword(findPasswordDto dto) {
		log.info("findPassword(dto={})", dto);
		
		User user = userRepo.FindPassword(dto.getUserid(), dto.getEmail(), dto.getPhone());
		
		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean UpdatePassword(UpdatePasswordDto dto) {
		log.info("UpdatePassword(dto={})", dto);

		User user = userRepo.save(dto.toEntity());

		if (user == null) {
			return false;
		} else {
			return true;
		}
	}

	@Transactional(readOnly = true)
	public exchangeInfoDto exchangeInfo(String userid) {
		log.info("exchangeInfo(userid={})", userid);
		
		User user = userRepo.selectByUserid(userid);

		exchangeInfoDto dto = exchangeInfoDto.fromEntity(user);
		log.info("exchangeDto: {}", dto);

		return dto;
	}

	@Transactional
	public int UpdatePoint(String userid, UpdatePointDto dto) {
		log.info("UpdatePoint(userid={}, dto={})", userid, dto);
		
		User entity = userRepo.findByUserid(userid);
		
		if (entity.getPassword().equals(dto.getPassword())) {
			entity.updatePoint(dto.getWithdraw());
			return 1;
		} else {
			return 0;
		}
	}

	@Transactional(readOnly = true)
	public List<AcceptListDto> AdminSignup() {
		log.info("AdminSignup()");
		
		List<User> list = userRepo.AdminSignup();

		return list.stream().map(AcceptListDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public List<ExchangeListDto> AdminExchange() {
		log.info("AdminExchange()");
		
		List<User> list = userRepo.AdminExchange();

		return list.stream().map(ExchangeListDto::fromEntity).toList();
	}

	@Transactional
	public void acceptUser(String userid, String accept) {
		log.info("acceptUser(userid={}, accept={})", userid, accept);
		
		User entity = userRepo.findByUserid(userid);
		
		try {
			entity.upgrade(accept);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void rejectUser(String userid) {
		log.info("rejectUser(userid={})", userid);
		
		User entity = userRepo.findByUserid(userid);
		entity.reject(userid);
	}

	public void acceptEx(UpdatePointDto dto) {
		try {
			userRepo.save(dto.toAcceptEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rejectEx(UpdatePointDto dto) {
		userRepo.save(dto.toRejectEntity());
	}
	
	@Transactional
	public void deleteUser(String userid) {
		log.info("deleteUser(userid={})", userid);
		
		User entity = userRepo.findByUserid(userid);
		
		userRepo.deleteById(entity.getId());
	}

	@Transactional(readOnly = true)
	public List<MemberListDto> AllMembers() {
		log.info("AllMembers()");
		
		List<User> list = userRepo.findAll();

		return list.stream().map(MemberListDto::fromEntity).toList();
	}

	public void setGrade(String userid, String grade) {
		log.info("setGrade(userid={}, grade={})", userid, grade);
		
		User entity = userRepo.findByUserid(userid);
		
		entity.updateGrade(grade);
	}

	public Map<String, String> getUserNicknames() {
		log.info("getUserNicknames()");
		
		Map<String, String> users = userRepo.findAllUserNicknames();
		log.info("users={}", users);
		
		return users;
	}

}
