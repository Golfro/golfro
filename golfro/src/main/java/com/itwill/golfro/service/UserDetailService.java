package com.itwill.golfro.service;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.UserSignInDto;
import com.itwill.golfro.dto.expertUserCreateDto;
import com.itwill.golfro.dto.normalUserCreateDto;
import com.itwill.golfro.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = userRepo.findByUserid(userid);
        log.debug("userid = {}", userid);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userid: " + userid);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserid(), user.getPassword(),
                new ArrayList<>()); // 권한을 빈 리스트로 설정
    }

    public User registerNewUser(User user) {
        if (userRepo.findByUserid(user.getUserid()) != null) { // 수정: 이미 존재하는 사용자 확인
            throw new RuntimeException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // BCrypt 인코딩
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
    
    public void nomalUserCreate(normalUserCreateDto dto) {
        log.info("nomalUserCreate(dto={})", dto);

        // DTO에서 사용자 엔티티로 변환
        User user = dto.toEntity();

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 엔티티를 저장
        userRepo.save(user);
    }

    public void expertUserCreate(expertUserCreateDto dto) throws Exception {
        log.info("expertUserCreate(dto={})", dto);

        // DTO에서 사용자 엔티티로 변환
        User user = dto.toEntity();

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            // 엔티티를 저장
            userRepo.save(user);
        } catch (Exception e) {
            throw new Exception("Failed to insert into pros table", e);
        }
    }
    
    public User read(UserSignInDto dto) {
        log.info("UserSignInDto(dto={})", dto);
        
        // 사용자 정보를 조회 (암호화된 비밀번호를 포함하여)
        User user = userRepo.findByUserid(dto.getUserid());

        // 사용자가 존재하는지 확인하고 비밀번호 검증
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return user;
        } else {
            return null;
        }
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
