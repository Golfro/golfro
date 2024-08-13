package com.itwill.golfro.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.itwill.golfro.domain.User;

public class UserQuerydslImpl extends QuerydslRepositorySupport implements UserQuerydsl {

	public UserQuerydslImpl() {
		super(User.class);
	}

	@Override
	public User selectProByUserid(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByUserid(String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByNickname(String nickname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByAccept(String accept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectByUseridAndPassword(String userid, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User FindUserid(String name, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User FindPassword(String userid, String email, String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsersPointRank() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> AdminSignup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> AdminExchange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> AllMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> findAllUserNicknames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String FindNicknameByUserId(String userid) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
