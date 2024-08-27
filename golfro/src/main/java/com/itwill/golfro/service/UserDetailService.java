package com.itwill.golfro.service;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		User user = userRepo.findByUserid(userid);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with userid: " + userid);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(),
				new ArrayList<>());
	}

	public User registerNewUser(User user) {
		if (userRepo.findByUserid(user.getUserid()) == null) {
			throw new RuntimeException("User already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepo.findById(id);
	}

	public User updateUser(User user) {
		return userRepo.save(user);
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	public boolean changePassword(String userid, String oldPassword, String newPassword) {
		User user = userRepo.findByUserid(userid);
		if (user != null) {
			if (passwordEncoder.matches(oldPassword, user.getPassword())) {
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepo.save(user);
				return true;
			}
		}
		return false;
	}
	
}