package com.itwill.golfro.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.dto.MyPostListSearchDto;
import com.itwill.golfro.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPostService {

	private final PostRepository postRepo;

	@Transactional(readOnly = true)
	public List<Post> search(MyPostListSearchDto dto) {
		log.info("search(dto={})", dto);
		List<Post> list = postRepo.search(dto);
		return list;
	}

	@Transactional(readOnly = true)
	public List<Post> selectAll() {
		log.info("selectAll()");
		List<Post> list = postRepo.findAll();
		return list;
	}

	@Transactional(readOnly = true)
	public Page<Post> getPagedPosts(String userid, int pageNo, Sort sort) {
		log.info("read(userid={}, pageNo={}, sort={})", userid, pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);

		Page<Post> list = postRepo.selectPagedPosts(userid, pageable);
		log.info("page.totalPages = {}", list.getTotalPages()); // 전체 페이지 개수
		log.info("page.number = {}", list.getNumber()); // 현재 페이지 번호
		log.info("page.hasPrevious = {}", list.hasPrevious()); // 이전 페이지가 있는 지 여부
		log.info("page.hasNext = {}", list.hasNext()); // 다음 페이지가 있는 지 여부
		
		return list;
	}
	
	@Transactional(readOnly = true)
	public long getTotalPostCount(String userid) {
		log.info("getTotalPostCount(userid={})", userid);
		return postRepo.selectTotalMyPostCount(userid);
	}
	
	public void delete(long id) {
		log.info("delete(id={})", id);
		postRepo.deleteById(id);
	}
	
}
