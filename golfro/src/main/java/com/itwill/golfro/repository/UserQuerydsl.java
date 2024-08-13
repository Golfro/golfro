package com.itwill.golfro.repository;

import java.util.List;
import java.util.Map;

import com.itwill.golfro.domain.User;

public interface UserQuerydsl {
	User selectProByUserid(String userid);
	
	User selectByUserid(String userid);
	
	User selectByNickname(String nickname);
	
	User selectByEmail(String email);
	
	User selectByPhone(String phone);
	
	User selectByAccept(String accept);
	
	User selectByUseridAndPassword(String userid, String password);
	
	User FindUserid(String name, String email);
	
	User FindPassword(String userid, String email, String phone);
	
	List<User> getUsersPointRank();
	
	List<User> AdminSignup();
	
	List<User> AdminExchange();
	
	List<User> AllMembers();
	
	Map<String, String> findAllUserNicknames();
	
	String FindNicknameByUserId(String userid);
}
