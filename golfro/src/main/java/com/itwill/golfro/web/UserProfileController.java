package com.itwill.golfro.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.MyPostListSearchDto;
import com.itwill.golfro.dto.UserProfileDto;
import com.itwill.golfro.dto.UserUpdateDto;
import com.itwill.golfro.service.CommPostService;
import com.itwill.golfro.service.MyCommentService;
import com.itwill.golfro.service.MyPostService;
import com.itwill.golfro.service.UserMypageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserProfileController {

	public static final String SESSION_ATTR_USER = "signedInUser";
	public static final String SESSION_USER_GRADE = "signedInUserGrade";
	private final UserMypageService userService;
	private final CommPostService commPostService;
	private final MyPostService myPostService;
	private final MyCommentService myCommentService;

	private final AmazonS3 amazonS3;
	private final String bucketName = "golfro-bucket";
	
	@GetMapping({ "/profile", "/privacy" })
	public void privacy(@RequestParam(required = false) String account, HttpSession session,
			Model model) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);
		String grade = (String) session.getAttribute(SESSION_USER_GRADE);

		if (grade.equals("G10")) {
			User pro = userService.readPro(userid);
			model.addAttribute("user", pro);
			log.info("user={}", pro);
		} else {
			User user = userService.read(userid);
			model.addAttribute("user", user);
			log.info("user={}", user);
		}

		model.addAttribute("account", account);
	}

	@GetMapping("/modify")
	public void details(HttpSession session, Model model) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);
		log.info("modify(userid={})", userid);

		String grade = (String) session.getAttribute(SESSION_USER_GRADE);
		if (grade.equals("G10")) {
			User pro = userService.readPro(userid);
			model.addAttribute("user", pro);
			log.info("user={}", pro);
		} else {
			User user = userService.read(userid);
			model.addAttribute("user", user);
			log.info("user={}", user);
		}
	}

	// 사용자 닉네임 중복체크 REST 컨트롤러
	@GetMapping("/checkname")
	@ResponseBody // 메서드 리턴 값이 클라이언트로 전달되는 데이터.
	public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
		log.info("checkNickname(nickname={})", nickname);

		boolean result = userService.checkNickname(nickname);
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@PostMapping("/update")
	public String update(UserUpdateDto dto, HttpSession session) {
		log.info("update(dto={})", dto);

		// 서비스 컴포넌트의 메서드를 호출해서 데이터베이스 테이블 업데이트를 수행.
		userService.update(dto);

		// 내 정보 페이지로 리다이렉트.
		return "redirect:/user/privacy?userid=" + (String) session.getAttribute(SESSION_ATTR_USER);
	}

	@PutMapping({ "/updateNickname", "/professional" })
	public ResponseEntity<Object> saveUserInfo(HttpSession session, @RequestBody UserProfileDto dto) {
		log.info("saveUserInfo(dto={})", dto);
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		dto.setUserid(userid);
		Object user = (Object) userService.updateProfile(dto);

		return ResponseEntity.ok(user);
	}

	@PostMapping("/file/image")
	public ResponseEntity<String> saveUserImage(HttpSession session, @RequestParam MultipartFile file) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		// 파일이 비어있는지 체크
		if (file.isEmpty()) {
			log.info("Please select a file to upload.");
		}

		try {
			// ObjectMetadata를 사용하여 파일의 Content-Length 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType()); // 파일의 Content-Type을 설정
			
			// 파일을 S3에 업로드
            amazonS3.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata));
			
            // S3에 저장된 파일의 URL을 생성
            String filePath = amazonS3.getUrl(bucketName, file.getOriginalFilename()).toString();
			File dest = new File(filePath);

			// 파일을 지정된 경로로 복사
			file.transferTo(dest);

			UserProfileDto dto = new UserProfileDto();
			dto.setUserid(userid);
			dto.setImage(filePath);

			userService.updateImage(dto);

			// 업로드 성공 메시지 전달
			log.info("File uploaded successfully: {}", file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
			// 업로드 실패 메시지 전달
			log.info("Failed to upload file: {}", file.getOriginalFilename());
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/file/image")
	@ResponseBody
	public Resource viewUserImage(@RequestParam String file) throws IOException {
		log.info("viewUserImage(file={})", file);

		Path path = Paths.get(file);
		log.info("path={}", path);

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return resource;
	}

	@GetMapping("/file/remove")
	@ResponseBody
	public ResponseEntity<String> removeUserImage(HttpSession session) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		String mediaPath = amazonS3.getUrl(bucketName, "basic_profile.png").toString();

		UserProfileDto dto = new UserProfileDto();
		dto.setUserid(userid);
		dto.setImage(mediaPath);

		userService.updateImage(dto);

		// 업로드 성공 메시지 전달
		log.info("Profile image removed successfully");

		return ResponseEntity.ok().build();
	}

	@GetMapping("/mylessons")
	public String myLessonList(@RequestParam String userid, HttpSession session) {
		String sessionUserid = (String) session.getAttribute(SESSION_ATTR_USER);

		log.info("myLessonList(userid={})", userid);

		return "redirect: ../mainPost/list?userid=" + sessionUserid;
	}

	@GetMapping("/myposts")
	public String myPostList(HttpSession session, @RequestParam(required = false) String keyword,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		log.info("myPostList(keyword={}, pageNo={})", keyword, pageNo);
		System.out.println(userid);

		Page<Post> posts;

		if (keyword != null && !keyword.isEmpty()) {
			System.out.println("if");
			MyPostListSearchDto searchDto = new MyPostListSearchDto();
			searchDto.setKeyword(keyword);
			posts = myPostService.search(searchDto, pageNo, Sort.by("id").descending());
		} else {
			System.out.println("else");
			posts = myPostService.getPagedPosts(userid, pageNo, Sort.by("id").descending());
		}

		model.addAttribute("posts", posts);
		model.addAttribute("keyword", keyword);

		return "/user/myPostList";
	}

	@GetMapping("/commentList")
	public void commentList(HttpSession session, Model model) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		log.info("commentList(userid={})", userid);

		List<Comment> list = myCommentService.commentReadByUserid(userid);
		model.addAttribute("comments", list);
	}

	@GetMapping("/announcements")
	public void announcements(Model model) {
		log.info("announcements()");

		List<Post> pinnedPosts = commPostService.Fixingthetop();

		model.addAttribute("pinnedPosts", pinnedPosts);
	}

	@GetMapping("/grade") // 원하는 URL 경로를 지정합니다.
	public String grade() {
		log.info("grade()");
		return "/user/grade"; // 뷰의 이름을 반환합니다. 여기서는 "mypage"라는 뷰 이름을 반환합니다.
	}

}
