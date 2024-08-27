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
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		User user = userRepository.findByUserid(userid);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with userid: " + userid);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(),
				new ArrayList<>());
	}

	public User registerNewUser(User user) {
		if (userRepository.findByUserid(user.getUserid()) == null) {
			throw new RuntimeException("User already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public boolean changePassword(String userid, String oldPassword, String newPassword) {
		User user = userRepository.findByUserid(userid);
		if (user != null) {
			if (passwordEncoder.matches(oldPassword, user.getPassword())) {
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}
}