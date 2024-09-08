package com.itwill.golfro.service;

import java.util.List;
import java.util.UUID;
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

		String[] category = { "P004" };
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
		log.info("mainCreate(dto={})", dto);
		String uuid = UUID.randomUUID().toString(); // 랜덤 스트링 생성 (파일명 중복되지 않게 파일명에 부착해주는 스트링)
		MultipartFile media = dto.getMedia(); // input media 네임의 값을 MultipartFile 타입으로 저장

		if (media != null && !media.isEmpty()) { // 만약 media의 값이 null이 아니면
			String mediaName = media.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고
			mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제
			int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서

			if (idx > 0) { // idx가 유효한지 확인
				String orgMediaName = mediaName.substring(0, idx); // 파일 이름의 첫 번째 인덱스부터 . 인덱스 전까지 저장
				String orgMediaType = mediaName.substring(idx); // 파일 이름의 . 다음 인덱스부터 확장자까지 저장
				String s3FileName = orgMediaName + uuid + orgMediaType; // 파일 순수 이름 + 랜덤 UUID + 확장자를 합쳐서 저장

				try {
					// ObjectMetadata를 사용하여 파일의 Content-Length 설정
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(media.getSize());
					metadata.setContentType(media.getContentType()); // 파일의 Content-Type을 설정

					// 파일을 S3에 업로드
					amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, media.getInputStream(), metadata));

					// S3에 저장된 파일의 URL을 생성
					String mediaPath = amazonS3.getUrl(bucketName, s3FileName).toString();
					dto.setMediaPath(mediaPath); // DTO에 S3 URL을 저장
				} catch (Exception e) {
					log.error("Failed to save media file", e);
					throw new RuntimeException("Failed to save media file", e);
				}
			} else {
				log.error("Invalid media file name: {}", mediaName);
				throw new IllegalArgumentException("Invalid media file name: " + mediaName);
			}
		}

		User user = userRepo.findByUserid(dto.getUserid());
		if (user == null) {
			throw new RuntimeException("User not found: " + dto.getUserid());
		}

		dto.setUser(user);

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

	@Transactional
	public void update(ReviewPostUpdateDto dto) {
		log.info("update(dto={})", dto);

		String uuid = UUID.randomUUID().toString(); // 랜덤 스트링 생성 (파일명 중복되지 않게 파일명에 부착해주는 스트링)
		log.info("3925번체크");
		MultipartFile media = dto.getMedia(); // input media 네임의 값을 MultipartFile 타입으로 저장
		log.info("0번체크");
		if (media != null && !media.isEmpty()) { // 만약 media의 값이 null이 아니면
			String mediaName = media.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고
			mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제
			int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서
			log.info("1번체크");

			if (idx > 0) { // idx가 유효한지 확인
				String orgMediaName = mediaName.substring(0, idx); // 파일 이름의 첫 번째 인덱스부터 . 인덱스 전까지 저장
				String orgMediaType = mediaName.substring(idx); // 파일 이름의 . 다음 인덱스부터 확장자까지 저장
				String s3FileName = orgMediaName + uuid + orgMediaType; // 파일 순수 이름 + 랜덤 UUID + 확장자를 합쳐서 저장
				log.info("2번체크");
				try {
					// ObjectMetadata를 사용하여 파일의 Content-Length 설정
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(media.getSize());
					metadata.setContentType(media.getContentType()); // 파일의 Content-Type을 설정

					// 파일을 S3에 업로드
					amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, media.getInputStream(), metadata));

					// S3에 저장된 파일의 URL을 생성
					String mediaPath = amazonS3.getUrl(bucketName, s3FileName).toString();
					dto.setMediaPath(mediaPath); // DTO에 S3 URL을 저장
				} catch (Exception e) {
					log.error("Failed to save media file", e);
					throw new RuntimeException("Failed to save media file", e);
				}
			} else {
				log.error("Invalid media file name: {}", mediaName);
				throw new IllegalArgumentException("Invalid media file name: " + mediaName);
			}
		}

		log.info("3번체크");
		log.info("4번체크 {}", dto.getCategoryId());
		Category category = ctgRepo.findById(dto.getCategoryId()).orElseThrow();
		log.info("5번체크 {}", category);
		Post entity = postRepo.findById(dto.getId()).orElseThrow();
		log.info("6번체크");

		log.info("파일경로={}", dto.getMediaPath());
		entity.update(dto.getTitle(), dto.getContent(), category, dto.getMediaPath());
	}

	@Transactional(readOnly = true)
	public Page<ReviewPostListDto> search(ReviewPostSearchDto dto, int pageNo, Sort sort) {
		log.info("search(dto={})", dto);

		Pageable pageable = PageRequest.of(pageNo, 10, sort);
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

		Pageable pageable = PageRequest.of(pageNo, 10, sort);
		Page<Post> list = postRepo.selectPagedPostsReview(pageable);
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
