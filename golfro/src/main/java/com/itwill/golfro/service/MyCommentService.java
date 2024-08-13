package com.itwill.golfro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyCommentService {

	private final CommentRepository cmtRepo;

	@Transactional(readOnly = true)
	public List<Comment> commentReadByUserid(String userid) {
		log.info("commentReadByUserid(userid={})", userid);
		List<Comment> list = cmtRepo.selectCommentsByUserid(userid);

		return list;
	}

}
