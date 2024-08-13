package com.itwill.golfro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.MainCommentCreateDto;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainCommentService {
	
	private final CommentRepository cmtRepo;
	private final UserRepository userRepo;
	
	public Comment mainPostCommentCreate(MainCommentCreateDto dto) {
		log.info("mainPostCommentCreate(dto={})", dto);
		cmtRepo.save(dto.toEntity());
		
		return dto.toEntity();
	}

	@Transactional(readOnly = true)
	public List<Comment> commentReadByPostId(long postId) {
		log.info("commentReadByPostId(postId={})", postId);
		List<Comment> list = cmtRepo.selectCommentsByPostId(postId);
		
		return list;
	}

	@Transactional(readOnly = true)
	public List<Comment> commentReadByUserid(String userid) {
		log.info("commentReadByUserid(userid={})", userid);
		List<Comment> list = cmtRepo.selectCommentsByUserid(userid);

		return list;
	}

	@Transactional
	public void selectCommentAndGiftPoint(long id) {
		log.info("selectCommentAndGiftPoint(id={})", id);
		
		Comment comment = cmtRepo.findById(id).orElseThrow();
		User entity = userRepo.findByUserid(comment.getUser().getUserid());
		
		entity.increasePoint(entity.getPoint());
	}
	
	public void deleteComment(long id) {
		log.info("deleteComment(id={})",id);
		cmtRepo.deleteById(id);
	}
	
	@Transactional
	public void editCommentById(CommentUpdateDto dto) {
		log.info("editCommentById(dto={})", dto);
    	
		Comment entity = cmtRepo.findById(dto.getId()).orElseThrow();
		
        entity.update(dto.getContent());
	}

}
