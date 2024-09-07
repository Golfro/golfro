package com.itwill.golfro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.MainCommentCreateDto;
import com.itwill.golfro.dto.MainCommentItemDto;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainCommentService {
	
	private final PostRepository postRepo;
	private final CommentRepository cmtRepo;
	private final UserRepository userRepo;
	
	public int mainPostCommentCreate(MainCommentCreateDto dto) {
		log.info("mainPostCommentCreate(dto={})", dto);
		
		try {
			cmtRepo.save(dto.toEntity());
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Transactional(readOnly = true)
	public List<MainCommentItemDto> commentReadByPostId(long postId) {
		log.info("commentReadByPostId(postId={})", postId);
		List<MainCommentItemDto> list = cmtRepo.selectCommentsByPostId(postId);
		
		return list;
	}

	@Transactional(readOnly = true)
	public List<Comment> commentReadByUserid(String userid) {
		log.info("commentReadByUserid(userid={})", userid);
		List<Comment> list = cmtRepo.selectCommentsByUserid(userid);

		return list;
	}

	@Transactional
	public void selectCommentAndGiftPoint(long commentsId) {
		log.info("selectCommentAndGiftPoint(id={})", commentsId);
		
		
		Comment comment = cmtRepo.findById(commentsId).orElseThrow();
		Post post = postRepo.findById(comment.getPost().getId()).get();
		User entity = userRepo.findByUserid(comment.getUser().getUserid());
		
		
		post.selectionConfirm();
		comment.selectionConfirm();
		entity.increasePoint(entity.getPoint());
		
	}
	
	public int deleteComment(long id) {
		log.info("deleteComment(id={})",id);
		
		try {
			cmtRepo.deleteById(id);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Transactional
	public int editCommentById(CommentUpdateDto dto) {
		log.info("editCommentById(dto={})", dto);
    	
		Comment entity = cmtRepo.findById(dto.getCommentId()).orElseThrow();
		
        try {
        	entity.update(dto.getContent());
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
