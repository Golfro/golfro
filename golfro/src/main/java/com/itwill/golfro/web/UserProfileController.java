package com.itwill.golfro.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Grade;
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
		Object userid = session.getAttribute(SESSION_ATTR_USER);
		Object grade = session.getAttribute(SESSION_USER_GRADE);

		if (grade.equals("G10")) {
			User pro = userService.readPro((String) userid);
			log.info("user={}", pro);
			model.addAttribute("user", pro);
			
			String birthStr = String.format("%08d", pro.getBirth());
			String formattedBirth = birthStr.substring(0, 4) + "." + birthStr.substring(4, 6) + "." + birthStr.substring(6, 8);
			model.addAttribute("birth", formattedBirth);
			
			String[] phoneParts = pro.getPhone().split("/");
	        String carrier = phoneParts[0];
	        String number = phoneParts[1];
	        model.addAttribute("carrier", carrier);
	        model.addAttribute("number", number);
	        
	        String[] addressParts = pro.getAddress().split("/");
	        String postcode = addressParts[0];
	        String address = addressParts[1];
	        String detailAddress = addressParts[2];
	        model.addAttribute("postcode", postcode);
	        model.addAttribute("address", address);
	        model.addAttribute("detailAddress", detailAddress);
	        
	        String[] AccountParts = pro.getAccount().split("/");
	        String bank = AccountParts[0];
	        String bankAccount = AccountParts[1];
	        model.addAttribute("bank", bank);
	        model.addAttribute("bankAccount", bankAccount);
		} else {
			User user = userService.read((String) userid);
			log.info("user={}", user);
			model.addAttribute("user", user);
			
			String birthStr = String.format("%08d", user.getBirth());
			String formattedBirth = birthStr.substring(0, 4) + "." + birthStr.substring(4, 6) + "." + birthStr.substring(6, 8);
			model.addAttribute("birth", formattedBirth);
			
			String[] phoneParts = user.getPhone().split("/");
	        String carrier = phoneParts[0];
	        String number = phoneParts[1];
	        model.addAttribute("carrier", carrier);
	        model.addAttribute("number", number);
	        
	        String[] addressParts = user.getAddress().split("/");
	        String postcode = addressParts[0];
	        String address = addressParts[1];
	        String detailAddress = addressParts[2];
	        model.addAttribute("postcode", postcode);
	        model.addAttribute("address", address);
	        model.addAttribute("detailAddress", detailAddress);
		}

		model.addAttribute("account", account);
	}

	@GetMapping("/modify")
	public void details(HttpSession session, Model model) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);
		log.info("modify(userid={})", userid);

		Grade grade = (Grade) session.getAttribute(SESSION_USER_GRADE);
		if (grade.getId().equals("G10")) {
			User pro = userService.readPro(userid);
			model.addAttribute("user", pro);
			log.info("user={}", pro);
			
			String birthStr = String.format("%08d", pro.getBirth());
			String formattedBirth = birthStr.substring(0, 4) + "." + birthStr.substring(4, 6) + "." + birthStr.substring(6, 8);
			model.addAttribute("birth", formattedBirth);
			
			String[] phoneParts = pro.getPhone().split("/");
	        String carrier = phoneParts[0];
	        String number = phoneParts[1];
	        model.addAttribute("carrier", carrier);
	        model.addAttribute("number", number);
	        
	        String[] addressParts = pro.getAddress().split("/");
	        String postcode = addressParts[0];
	        String address = addressParts[1];
	        String detailAddress = addressParts[2];
	        model.addAttribute("postcode", postcode);
	        model.addAttribute("address", address);
	        model.addAttribute("detailAddress", detailAddress);
	        
	        String[] AccountParts = pro.getAccount().split("/");
	        String bank = AccountParts[0];
	        String bankAccount = AccountParts[1];
	        model.addAttribute("bank", bank);
	        model.addAttribute("bankAccount", bankAccount);
		} else {
			User user = userService.read(userid);
			model.addAttribute("user", user);
			log.info("user={}", user);
			
			String birthStr = String.format("%08d", user.getBirth());
			String formattedBirth = birthStr.substring(0, 4) + "." + birthStr.substring(4, 6) + "." + birthStr.substring(6, 8);
			model.addAttribute("birth", formattedBirth);
			
			String[] phoneParts = user.getPhone().split("/");
	        String carrier = phoneParts[0];
	        String number = phoneParts[1];
	        model.addAttribute("carrier", carrier);
	        model.addAttribute("number", number);
	        
	        String[] addressParts = user.getAddress().split("/");
	        String postcode = addressParts[0];
	        String address = addressParts[1];
	        String detailAddress = addressParts[2];
	        model.addAttribute("postcode", postcode);
	        model.addAttribute("address", address);
	        model.addAttribute("detailAddress", detailAddress);
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
		userService.update(dto.toEntity());

		// 내 정보 페이지로 리다이렉트.
		return "redirect:/user/privacy";
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
	public ResponseEntity<String> saveUserImage(HttpSession session, @RequestParam("file") MultipartFile mediaFile) {
		String userid = (String) session.getAttribute(SESSION_ATTR_USER);

		userService.uploadImage(userid, mediaFile);
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("/file/image")
	public ResponseEntity<Resource> viewUserImage(@RequestParam String file) {
	    try {
	        // URL 유효성 검사
	        if (file == null || !file.startsWith("http://") && !file.startsWith("https://")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 유효하지 않은 URL에 대한 처리
	        }

	        // URI 객체 생성
	        URI uri = new URI(file);
	        if (!uri.isAbsolute()) {
	            throw new IllegalArgumentException("URI is not absolute");
	        }
	        
	        URL url = uri.toURL();

	        // HTTP 연결 설정
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        // 응답 코드 확인
	        int responseCode = connection.getResponseCode();
	        if (responseCode != HttpURLConnection.HTTP_OK) {
	            return ResponseEntity.status(responseCode).body(null); // 적절한 응답 코드 반환
	        }

	        // 응답 처리
	        try (InputStream inputStream = connection.getInputStream();
	             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }

	            byte[] imageBytes = outputStream.toByteArray();
	            ByteArrayResource resource = new ByteArrayResource(imageBytes);

	            // 파일 타입 및 헤더 설정
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.IMAGE_JPEG); // 이미지 타입을 적절히 설정하세요.
	            headers.setContentLength(imageBytes.length);

	            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	        }
	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 잘못된 URL 처리
	    } catch (IOException | URISyntaxException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 기타 오류 처리
	    }
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

		return "redirect:/mainPost/list?userid=" + sessionUserid;
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
