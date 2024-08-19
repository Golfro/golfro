package com.itwill.golfro.service;

import java.util.List;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommentCreateDto;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.ReviewPostCreateDto;
import com.itwill.golfro.dto.ReviewPostListDto;
import com.itwill.golfro.dto.ReviewPostSearchDto;
import com.itwill.golfro.dto.ReviewPostUpdateDto;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewPostService {

	private final PostRepository postRepo;
	private final CommentRepository cmtRepo;
	private final UserRepository userRepo;
	private final MediaService mediaService;

	@Transactional(readOnly = true)
	public Page<Post> read(int pageNo, Sort sort) {
		log.info("read()");
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		String[] category = {"P004"}; 
		Page<Post> posts = postRepo.selectOrderByIdDesc(category, pageable);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public Post read(long id) {
		log.info("read(id={})", id);
		return postRepo.findById(id).orElseThrow();
	}

	@Transactional
	public void Create(ReviewPostCreateDto dto) {
		log.info("Create(dto={})", dto);

		if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
			String fileName = mediaService.storeFile(dto.getMedia());
			dto.setMediaPath(fileName);
		}

		postRepo.save(dto.toEntity());
	}

	@Transactional(readOnly = true)
	public List<Post> Fixingthetop() {
		log.info("Fixingthetop()");
		List<Post> list = postRepo.Fixingthetop();
		
		return list;
	}

	public void delete(long id) {
		log.info("delete(id={})", id);
		
		postRepo.deleteById(id);
	}

	public void update(ReviewPostUpdateDto dto) {
		log.info("update(dto={})", dto);
		
		Post result = postRepo.save(dto.toEntity());
		log.info("update 결과: {}", result);
	}

	@Transactional(readOnly = true)
	public Page<ReviewPostListDto> search(ReviewPostSearchDto dto, int pageNo, Sort sort) {
		log.info("search(dto={})", dto);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		Page<Post> list = postRepo.search(dto, pageable);
		Page<ReviewPostListDto> posts = list.map(ReviewPostListDto::fromEntity);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public Post getPreviousPost(String[] categories, LocalDateTime createdTime) {
		log.info("getPreviousPost(categories={}, createdTime={})", categories, createdTime);
		return postRepo.findPreviousPost(categories, createdTime);
	}

	@Transactional(readOnly = true)
	public Post getNextPost(String[] categories, LocalDateTime createdTime) {
		log.info("getNextPost(categories={}, createdTime={})", categories, createdTime);
		return postRepo.findNextPost(categories, createdTime);
	}

	@Transactional
	public void increaseViews(long id) {
		log.info("increaseViews(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		entity.increaseViews(entity.getViews());
	}

	@Transactional
	public void increaseLikes(long id) {
		log.info("increaseLikes(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		entity.increaseLikes(entity.getLikes());
	}

	@Transactional(readOnly = true)
	public Comment readComment(long id) {
		log.info("readComment(id={})", id);
		
		Comment comment = cmtRepo.findById(id).orElseThrow();
		
		return comment;
	}

	@Transactional(readOnly = true)
	public List<Comment> readAllComment(long postId) {
		log.info("readAllComment(postId={})", postId);
		
		List<Comment> list = cmtRepo.findByPostId(postId);
		
		return list;
	}

	public void inserComment(CommentCreateDto dto) {
		log.info("inserComment(dto={})", dto);
		cmtRepo.save(dto.toEntity());
	}

	public void updateComment(CommentUpdateDto dto) {
		log.info("updateComment(dto={})", dto);
		cmtRepo.save(dto.toEntity());
	}

	public void deleteCommentById(long id) {
		log.info("deleteCommentById(id={})", id);
		cmtRepo.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Page<ReviewPostListDto> getPagedPosts(int pageNo, Sort sort) {
		log.info("getPagedPosts(pageNo={}, sort={})", pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		Page<Post> list = postRepo.selectPagedPosts(pageable);
		Page<ReviewPostListDto> posts = list.map(ReviewPostListDto::fromEntity);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public long getTotalPostCount(String[] categories) {
		log.info("getTotalPostCount(categories={})", (Object) categories);
		return postRepo.selectTotalPostCount(categories);
	}

	@Transactional(readOnly = true)
	public long selectCommentCount(long id) {
		log.info("selectCommentCount(id={})", id);
		return cmtRepo.selectCommentCount(id);
	}

	@Transactional(readOnly = true)
	public User getLoggedInUser(String userid) {
		log.info("selectCommentCount(id={})", userid);
		return userRepo.selectByUserid(userid);
	}

}
