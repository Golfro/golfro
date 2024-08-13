package com.itwill.golfro.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.dto.MainPostCreateDto;
import com.itwill.golfro.dto.MainPostSearchDto;
import com.itwill.golfro.dto.MainPostUpdateDto;
import com.itwill.golfro.dto.MyPostSearchDto;
import com.itwill.golfro.repository.ClubRepository;
import com.itwill.golfro.repository.PostRepository;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainPostService {

	private final PostRepository postRepo;
	private final ClubRepository clubRepo;
	
	public void mainCreate(MainPostCreateDto dto) {
		log.info("mainCreate(dto={})", dto);
		String uuid = UUID.randomUUID().toString(); // 랜덤스트림 생성(파일명 중복되지 않게 파일명에 부착해주는 스트링)
		MultipartFile media = dto.getMedia(); // input media 네임의 값을 MultipartFile 타입으로 저장
		
		if (media != null && !media.isEmpty()) { // 만약 media의 값이 null이 아니면
			String mediaName = media.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고,
			mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제를 하고
			int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서

			if (idx > 0) { // idx가 유효한지 확인
				String orgMediaName = mediaName.substring(0, idx); // file 이름에 첫번째 인덱스부터 . 인덱스 전까지만 orgMediaName 변수에 저장
				String orgMediaType = mediaName.substring(idx); // file 이름에 . 다음 인덱스부터 다음 인덱스 까지(확장자)를 해당 변수애 저장
				String sMediaName = orgMediaName + uuid + orgMediaType; // 파일 순수 이름 + 랜덤으로 생성된 uuid + 확장자를 합쳐서 한 변수에 저장
				String realPath = "C:\\Users\\itwill\\Desktop\\media"; // 실제 파일이 저장될 경로를 변수에 저장

				File folder = new File(realPath);

				if (!folder.exists()) { // 만약 설정한 경로에 저장 폴더가 존재하지 않는다면,
					folder.mkdirs(); // 새폴더를 생성
				}
				
				File file = new File(realPath + "/" + sMediaName);

				try {
					media.transferTo(file);
					String mediaPath = realPath + "/" + sMediaName;
					dto.setMediaPath(mediaPath); // DTO에 파일 경로 설정
				} catch (Exception e) {
					log.error("Failed to save media file", e);
					throw new RuntimeException("Failed to save media file", e);
				}
			} else {
				log.error("Invalid media file name: {}", mediaName);
				throw new IllegalArgumentException("Invalid media file name: " + mediaName);
			}
		}
		
		postRepo.save(dto.toEntity());
	}

	public void mainPostUpdate(MainPostUpdateDto dto) {
		log.info("mainPostUpdate(dto={})", dto);
		String uuid = UUID.randomUUID().toString(); // 랜덤스트림 생성(파일명 중복되지 않게 파일명에 부착해주는 스트링)
		MultipartFile media = dto.getMedia(); // input media 네임의 값을 MultipartFile 타입으로 저장
		
		if (media != null && !media.isEmpty()) { // 만약 media의 값이 null이 아니면
			String mediaName = media.getOriginalFilename(); // mediaName 변수에 media 이름을 값을 가지고 오고,
			mediaName = mediaName.replaceAll(" ", ""); // 가지고 온 media 이름에 공백을 모두 삭제를 하고
			int idx = mediaName.lastIndexOf("."); // 이름에서 마지막 . index를 찾아서

			if (idx > 0) { // idx가 유효한지 확인
				String orgMediaName = mediaName.substring(0, idx); // file 이름에 첫번째 인덱스부터 . 인덱스 전까지만 orgMediaName 변수에 저장
				String orgMediaType = mediaName.substring(idx); // file 이름에 . 다음 인덱스부터 다음 인덱스 까지(확장자)를 해당 변수애 저장
				String sMediaName = orgMediaName + uuid + orgMediaType; // 파일 순수 이름 + 랜덤으로 생성된 uuid + 확장자를 합쳐서 한 변수에 저장
				String realPath = "C:\\Users\\itwill\\Desktop\\media"; // 실제 파일이 저장될 경로를 변수에 저장
				
				File folder = new File(realPath);

				if (!folder.exists()) { // 만약 설정한 경로에 저장 폴더가 존재하지 않는다면,
					folder.mkdirs(); // 새폴더를 생성
				}
				
				File file = new File(realPath + "/" + sMediaName);

				try {
					media.transferTo(file);
					String mediaPath = realPath + "/" + sMediaName;
					dto.setMediaPath(mediaPath); // DTO에 파일 경로 설정
				} catch (Exception e) {
					log.error("Failed to save media file", e);
					throw new RuntimeException("Failed to save media file", e);
				}
			} else {
				log.error("Invalid media file name: {}", mediaName);
				throw new IllegalArgumentException("Invalid media file name: " + mediaName);
			}
		}
		
		postRepo.save(dto.toEntity());
	}

	@Transactional(readOnly = true)
	public List<Club> clubTypes() {
		log.info("clubTypes()");
		return clubRepo.findAll();
	}

	@Transactional(readOnly = true)
	public List<Tuple> readAll() {
		log.info("readAll()");
		
		List<Tuple> list = postRepo.selectReadAll();
		
		return list;
	}

	@Transactional(readOnly = true)
	public List<Tuple> readAllByUserid(String userid) {
		log.info("readAllByUserid(userid={})", userid);
		
		List<Tuple> list = postRepo.selectReadAllByUserid(userid);
		
		return list;
	}

	@Transactional
	public Tuple selectPostId(long id) {
		log.info("selectPostId(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		entity.increaseViews(entity.getViews());

		Tuple post = postRepo.selectByPostId(id);
		
		return post;
	}

	public void deleteById(long id) {
		log.info("deleteById(id={})", id);
		
		postRepo.deleteById(id);
	}

	@Transactional
	// 좋아요 업데이트 메서드
	public void updatePostLikes(long id) {
		log.info("updatePostLikes(id={})", id);
		
		Post entity = postRepo.findById(id).orElseThrow();
		entity.increaseLikes(entity.getLikes());
	}

	@Transactional(readOnly = true)
	// 좋아요 불러오는 메서드
	public long getPostLikes(long postId) {
		log.info("getPostLikes(postId={})", postId);
		return postRepo.selectLikes(postId);
	}

	@Transactional(readOnly = true)
	public List<Tuple> searchRead(MainPostSearchDto dto) {
		log.info("searchRead(dto={})", dto);
		
		List<Tuple> list = postRepo.search(dto);
		
		return list;
	}

	@Transactional(readOnly = true)
	public List<Tuple> searchReadByUserid(MyPostSearchDto dto) {
		log.info("search(dto={})", dto);
		
		List<Tuple> list = postRepo.searchMyPost(dto);
		
		return list;
	}

	@Transactional(readOnly = true)
	public Page<Tuple> getPagedPosts(int pageNo, Sort sort) {
		log.info("getPagedPosts(pageNo={}, sort={})", pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		Page<Tuple> list = postRepo.getPostList(pageable);
		
		return list;
	}

	@Transactional(readOnly = true)
	public Page<Tuple> getPagedPostsByUserid(String userid, int pageNo, Sort sort) {
		log.info("getPagedPostsByUserid(userid={}, pageNo={}, sort={})", userid, pageNo, sort);
		
		Pageable pageable = PageRequest.of(pageNo, 5, sort);
		
		Page<Tuple> list = postRepo.getPostListByUserid(userid, pageable);
		
		return list;
	}

	@Transactional(readOnly = true)
	public long getTotalPostCount() {
		log.info("getTotalPostCount()");
		return postRepo.getTotalCount();
	}

	@Transactional(readOnly = true)
	public long getTotalPostCountByUserid(String userid) {
		log.info("getTotalPostCountByUserid(userid={})", userid);
		return postRepo.getTotalCountByUserid(userid);
	}

}
