package com.itwill.golfro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.itwill.golfro.domain.Club;
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
import com.itwill.golfro.repository.ClubRepository;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.repository.PostRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommPostService {
	
	
	private final AmazonS3 amazonS3;
	private final String bucketName = "golfro-bucket";
	
	private final PostRepository postRepo;
	private final CommentRepository cmtRepo;
	private final UserRepository userRepo;
	private final CategoryRepository ctgRepo;
	private final MediaService mediaService;
	private final ClubRepository clubRepo;
	
	@Transactional(readOnly = true)
	public Page<Post> read(int pageNo, Sort sort) {
		log.info("read()");
		
		Pageable pageable = PageRequest.of(pageNo, 10, sort);
		
		String[] categories = {"F001", "F002", "F003"};
		Page<Post> posts = postRepo.selectOrderByIdDesc(categories, pageable);
		
		return posts;
	}
	
	@Transactional(readOnly = true)
	public Post read(long id) {
		log.info("read(id={})", id);
		return postRepo.selectById(id);
	}
	
	
	
	

	public void Create(CommPostCreateDto dto) {
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
	    if(user == null) {
            throw new RuntimeException("User not found: " + dto.getUserid());
	    }
	    
	    dto.setUser(user);
	    
	    postRepo.save(dto.toEntity());
		
		
//		log.info("CommPostCreate(dto={})", dto);
//
//		if (dto.getMedia() != null && !dto.getMedia().isEmpty()) {
//			String fileName = mediaService.storeFile(dto.getMedia());
//			dto.setMediaPath(fileName);
//		}
//
//		postRepo.save(dto.toEntity());
//		
//		return dto.toEntity();
	}
	
	
	
	
	
	
	
	
	

	public void delete(long id) {
		log.info("delete(id={})", id);
		postRepo.deleteById(id);
	}

	@Transactional
	public void update(CommPostUpdateDto dto) {
		log.info("update(dto={})", dto);
		
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
		
				
		Category category = ctgRepo.findById(dto.getCategoryId()).orElseThrow();
		Post entity = postRepo.findById(dto.getId()).orElseThrow();
		
		log.info("파일경로={}", dto.getMediaPath());
		entity.update(dto.getTitle(),dto.getContent(),category,dto.getMediaPath());
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
	public Page<CommPostListDto> searchByCategoryAndKeyword(CommPostSearchDto dto, int pageNo, Sort sort) {
		log.info("searchByCategoryAndKeyword(dto={}, pageNo, sort)", dto, pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 10, sort);
		Page<Post> list = postRepo.selectByCategoryAndKeyword(dto, pageable);
		Page<CommPostListDto> posts = list.map(CommPostListDto::fromEntity);
		
		return posts;
	}

	@Transactional(readOnly = true)
	public Post getPreviousPost(LocalDateTime createdTime) {
		log.info("getPreviousPost(createdTime={})", createdTime);
		String[] categories = {"F001", "F002", "F003"};
		
		return postRepo.findPreviousPostComm(categories, createdTime);
	}
	
	@Transactional(readOnly = true)
	public Post getNextPost(LocalDateTime createdTime) {
		log.info("getNextPost(createdTime={})", createdTime);
		String[] categories = {"F001", "F002", "F003"};
		
		return postRepo.findNextPostComm(categories, createdTime);
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
		Pageable pageable = PageRequest.of(pageNo, 10, sort);
		
		// 영속성(persistence/repository) 계층의 메서드를 호출해서 엔터티들의 리스트를 가져옴.
		Page<Post> list = postRepo.selectPagedPostsComm(pageable);
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
