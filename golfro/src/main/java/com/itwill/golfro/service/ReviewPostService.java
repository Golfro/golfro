package com.itwill.golfro.service;

import java.util.List;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommentCreateDto;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.ReviewPostCreateDto;
import com.itwill.golfro.dto.ReviewPostListDto;
import com.itwill.golfro.dto.ReviewPostSearchDto;
import com.itwill.golfro.dto.ReviewPostUpdateDto;
import com.itwill.golfro.repository.CategoryRepository;
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
	private final CategoryRepository ctgRepo;

	private final AmazonS3 amazonS3;
	private final String bucketName = "golfro-bucket";
	
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
	public void Create(ReviewPostCreateDto dto, MultipartFile mediaFile) {
		log.info("Create(dto={})", dto);

		// 파일이 비어있는지 체크
		if (mediaFile.isEmpty()) {
			log.info("Please select a file to upload.");
		}
	
		String mediaName = mediaFile.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고
        mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제
        int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서
		
        if (idx > 0) { // idx가 유효한지 확인
            String orgMediaName = mediaName.substring(0, idx); // 파일 이름의 첫 번째 인덱스부터 . 인덱스 전까지 저장
            String orgMediaType = mediaName.substring(idx); // 파일 이름의 . 다음 인덱스부터 확장자까지 저장
            String s3FileName = orgMediaName + orgMediaType; // 파일 순수 이름 + 랜덤 UUID + 확장자를 합쳐서 저장
        
			try {
				// ObjectMetadata를 사용하여 파일의 Content-Length 설정
		        ObjectMetadata metadata = new ObjectMetadata();
		        metadata.setContentLength(mediaFile.getSize());
		        metadata.setContentType(mediaFile.getContentType()); // 파일의 Content-Type을 설정
				
				// 파일을 S3에 업로드
		        amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, mediaFile.getInputStream(), metadata));
				
		        // S3에 저장된 파일의 URL을 생성
		        String mediaPath = amazonS3.getUrl(bucketName, s3FileName).toString();
		
				dto.setMediaPath(mediaPath);
		
				// 업로드 성공 메시지 전달
				log.info("File uploaded successfully: {}", mediaFile.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
				// 업로드 실패 메시지 전달
				log.info("Failed to upload file: {}", mediaFile.getOriginalFilename());
			} 
        } else {
        	log.error("Invalid media file name: {}", mediaName);
        	throw new IllegalArgumentException("Invalid media file name: " + mediaName);
        }

		User user = userRepo.findByUserid(dto.getUserid());
		Category category = ctgRepo.findById(dto.getCategoryId()).orElseThrow();
		
		dto.setUser(user);
		dto.setCategory(category);
		
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
		
		List<Comment> list = cmtRepo.selectAllComments(postId);
		
		return list;
	}

	// 리뷰 게시판 댓글 작성 메서드
	@Transactional
	public Comment insertComment(CommentCreateDto dto) {
		log.info("insertComment(dto={})", dto);
		
//		User user = userRepo.findByUserid(dto.getUserid());
//		dto.setUser(user);
//		Post post = postRepo.findById(dto.getId()).orElseThrow();
//		dto.setPost(post);
		
		return cmtRepo.save(dto.toEntity());
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
