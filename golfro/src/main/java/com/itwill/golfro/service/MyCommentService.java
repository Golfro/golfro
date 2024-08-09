package com.itwill.golfro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwill.golfro.repository.MyComment;
import com.itwill.golfro.repository.MyCommentDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyCommentService {

	private final MyCommentDao myCommentDao;

	public List<MyComment> commentReadByUserid(String userid) {
		log.debug("commentReadByUserid()");
		List<MyComment> list = myCommentDao.selectCommentsByUserid(userid);

		return list;
	}

}
