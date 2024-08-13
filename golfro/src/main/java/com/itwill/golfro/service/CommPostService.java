package com.itwill.golfro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommPostCreateDto;
import com.itwill.golfro.dto.CommPostListDto;
import com.itwill.golfro.dto.CommPostSearchDto;
import com.itwill.golfro.dto.CommPostUpdateDto;
import com.itwill.golfro.dto.CommentCreateDto;
import com.itwill.golfro.dto.CommentItemDto;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.repository.CategoryRepository;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommPostService {

	private final PostRepository postRepo;
	private final CommentRepository cmtRepo;
	private final UserRepository userRepo;
	private final CategoryRepository ctgRepo;
	private final MediaService mediaService;
	
	@Transactional(readOnly = true)
	public List<Post> read() {
		log.info("read()");
		List<Post> list = postRepo.selectOrderByIdDesc();
		
		return list;
	}
	
	@Transactional(readOnly = true)
	public Post read(long id) {
		log.info("read(id={})", id);
		return postRepo.selectById(id);
	}

	public Post Create(CommPostCreateDto dto) {
		log.info("CommPostCreate(dto={})", dto);

		if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
			String fileName = mediaService.storeFile(dto.getMedia());
			dto.setMediaPath(fileName);
		}

		postRepo.save(dto.toEntity());
		
		return dto.toEntity();
	}

	public void delete(long id) {
		log.info("delete(id={})", id);
		postRepo.deleteById(id);
	}

	@Transactional
	public void update(CommPostUpdateDto dto) {
		log.info("update(dto={})", dto);

		if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
			String fileName = mediaService.storeFile(dto.getMedia());
			dto.setMediaPath(fileName);
		}

		Post entity = postRepo.findById(dto.getId()).orElseThrow();
		
		entity.update(dto.getTitle(), dto.getContent()
				, Category.builder().id(dto.getCategoryId()).build(), dto.getMediaPath());
	}

	@Transactional(readOnly = true)
	public List<Post> getTop5ByF001() {
		log.info("getTop5ByF001()");
		return postRepo.selectTop5ByF001();
	}
	
	@Transactional(readOnly = true)
	public List<Post> Fixingthetop() {
		log.info("Fixingthetop()");
		return postRepo.Fixingthetop();
	}

	@Transactional(readOnly = true)
	public List<Post> getTop5ByF002() {
		log.info("getTop5ByF002()");
		return postRepo.selectTop5ByF002();
	}

	@Transactional(readOnly = true)
	public List<Post> searchByCategoryAndKeyword(CommPostSearchDto dto) {
		log.info("searchByCategoryAndKeyword(dto={})", dto);
		return postRepo.selectByCategoryAndKeyword(dto);
	}

	@Transactional(readOnly = true)
	public Post getPreviousPost(LocalDateTime createdTime) {
		log.info("getPreviousPost(createdTime={})", createdTime);
		String[] categories = {"F001", "F002", "F003"};
		
		return postRepo.findPreviousPost(categories, createdTime);
	}
	
	@Transactional(readOnly = true)
	public Post getNextPost(LocalDateTime createdTime) {
		log.info("getNextPost(createdTime={})", createdTime);
		String[] categories = {"F001", "F002", "F003"};
		
		return postRepo.findNextPost(categories, createdTime);
	}

	@Transactional
	public void increaseViews(long id) {
		log.info("increaseViews(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		
		// 검색된 엔터티의 필드를 업데이트: views + 1
		entity.increaseViews(entity.getViews());
	}

	@Transactional
	public void increaseLikes(long id) {
		log.info("increaseLikes(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		
		// 검색된 엔터티의 필드를 업데이트: likes + 1
		entity.increaseLikes(entity.getLikes());
	}

	@Transactional(readOnly = true)
	public CommentItemDto readComment(long id) {
		log.info("readComment(id={})", id);
		Comment comment = cmtRepo.selectCommentById(id);
		
		return CommentItemDto.fromEntity(comment);
	}

	@Transactional(readOnly = true)
	public List<Comment> readAllComment(long postId) {
		log.info("readAllComment(postId={})", postId);
		return cmtRepo.selectByPostId(postId);
	}

	public Comment inserComment(CommentCreateDto dto) {
		log.info("inserComment(dto={})", dto);
		return cmtRepo.save(dto.toEntity());
	}

	public Comment updateComment(CommentUpdateDto dto) {
		log.info("updateComment(dto={})", dto);
		return cmtRepo.save(dto.toEntity());
	}

	public void deleteCommentById(long id) {
		log.info("deleteCommentById(id={})", id);
		cmtRepo.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Page<CommPostListDto> getPagedPosts(int pageNo, Sort sort) {
		log.info("read(pageNo={}, sort={})", pageNo, sort);
		
		// Pageable 객체 생성
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		// 영속성(persistence/repository) 계층의 메서드를 호출해서 엔터티들의 리스트를 가져옴.
		Page<Post> list = postRepo.selectPagedPosts(pageable);
		log.info("page.totalPages = {}", list.getTotalPages()); // 전체 페이지 개수
		log.info("page.number = {}", list.getNumber()); // 현재 페이지 번호
		log.info("page.hasPrevious = {}", list.hasPrevious()); // 이전 페이지가 있는 지 여부
		log.info("page.hasNext = {}", list.hasNext()); // 다음 페이지가 있는 지 여부
		
		// Page<Post> 객체를 Page<CommPostListDto> 타입으로 변환.
		Page<CommPostListDto> posts = list.map(CommPostListDto::fromEntity);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public long getTotalPostCount() {
		log.info("getTotalPostCount()");
		String[] categories = {"F001", "F002", "F003"};
		
		return postRepo.selectTotalPostCount(categories);
	}

	@Transactional(readOnly = true)
	public long selectCommentCount(long id) {
		log.info("selectCommentCount(id={})", id);
		return cmtRepo.selectCommentCount(id);
	}

	@Transactional(readOnly = true)
	public User getLoggedInUser(String userId) {
		log.info("getLoggedInUser(userId={})", userId);
		return userRepo.selectByUserid(userId);
	}
	
	@Transactional(readOnly = true)
	public Map<String, String> catrgoryname() {
		log.info("catrgoryname()");
		
		List<Category> categories = ctgRepo.findAll();
		
		Map<String, String> result = categories.stream()
	            .collect(Collectors.toMap(
	                Category::getId,    // 키를 Category의 ID로 설정
	                Category::getName   // 값을 Category의 NAME으로 설정
	            ));
		log.info("result={}", result);
		
		return result;
	}

}
