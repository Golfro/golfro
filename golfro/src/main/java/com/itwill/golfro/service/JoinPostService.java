package com.itwill.golfro.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.JoinPostCreateDto;
import com.itwill.golfro.dto.JoinPostSearchDto;
import com.itwill.golfro.dto.JoinPostUpdateDto;
import com.itwill.golfro.repository.CategoryRepository;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinPostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final CategoryRepository ctgRepo;

    @Transactional(readOnly = true)
	public Page<Post> read(int pageNo, Sort sort) {
		log.info("read()");
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		String[] category = {"P003"};
		Page<Post> posts = postRepo.selectOrderByIdDesc(category, pageable);
		
		return posts;
	}

    @Transactional(readOnly = true)
	public Post read(long id) {
		log.info("read(id={})", id);
		return postRepo.findById(id).orElseThrow();
	}

	@Transactional(readOnly = true)
	public Page<Post> search(JoinPostSearchDto dto, int pageNo, Sort sort) {
		log.info("search(dto={})", dto);

		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		Page<Post> list = postRepo.search(dto, pageable);
		log.info("Raw search results: {}", list);

		return list;
	}
	
	public Post create(JoinPostCreateDto dto) {
		log.info("create(dto={})", dto);

		Post result = new Post();
		try {
			dto.setUser(userRepo.findByUserid(dto.getUserid()));
			dto.setCategory(ctgRepo.findById(dto.getCategoryId()).orElseThrow());
			result = postRepo.save(dto.toEntity());
			log.info("insert 결과: {}", result);
		} catch (Exception e) {
			log.error("Error during insertPost: ", e);
		}

		return result;
	}

	public void delete(long id) {
		log.info("delete(id={})", id);
		postRepo.deleteById(id);
	}

	public void update(JoinPostUpdateDto dto) {
		log.info("update(dto={})", dto);
		
		Post result = postRepo.save(dto.toEntity());
		log.info("update 결과: {}", result);
	}

	@Transactional(readOnly = true)
	public Post getPreviousPost(String[] categories, LocalDateTime teeoff) {
		log.info("getPreviousPost(category={}, teeoff={})", categories, teeoff);
		return postRepo.findPreviousPost(categories, teeoff);
	}

	@Transactional(readOnly = true)
	public Post getNextPost(String[] categories, LocalDateTime teeoff) {
		log.info("getNextPost(category={}, teeoff={})", categories, teeoff);
		return postRepo.findNextPost(categories, teeoff);
	}

	@Transactional(readOnly = true)
	public Page<Post> findByTeeoffDate(LocalDate teeoffDate, int pageNo, Sort sort) {
		log.info("findByTeeoffDate(teeoffDate={})", teeoffDate);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		Page<Post> posts = postRepo.selectByTeeoffDate(teeoffDate, pageable);
		
		return posts;
	}
	
	@Transactional(readOnly = true)
	public Page<Post> getPagedPosts(int pageNo, Sort sort) {
		log.info("getPagedPosts(pageNo={}, sort={})", pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		Page<Post> posts = postRepo.selectPagedPosts(pageable);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public long getTotalPostCount(String[] categories) {
		log.info("getTotalPostCount()");
		return postRepo.selectTotalPostCount(categories);
	}
	
	@Transactional(readOnly = true)
	public User getLoggedInUser(String userid) {
		log.info("getLoggedInUser(userid={})", userid);
		return userRepo.selectByUserid(userid);
	}

}
