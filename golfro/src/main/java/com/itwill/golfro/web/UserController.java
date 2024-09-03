package com.itwill.golfro.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.UpdatePasswordDto;
import com.itwill.golfro.dto.UpdatePointDto;
import com.itwill.golfro.dto.UserSignInDto;
import com.itwill.golfro.dto.exchangeInfoDto;
import com.itwill.golfro.dto.expertUserCreateDto;
import com.itwill.golfro.dto.findIdDto;
import com.itwill.golfro.dto.findPasswordDto;
import com.itwill.golfro.dto.normalUserCreateDto;
import com.itwill.golfro.service.UserDetailService;
import com.itwill.golfro.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private static final String SESSION_ATTR_USERID = "signedInUserId";
	private static final String SESSION_ATTR_USER = "signedInUser";
	private static final String SESSION_USER_GRADE = "signedInUserGrade";
	public static int SESSION_TIME = 30 * 60; // 30분

	private final UserService userService;
	private final UserDetailService userDService;

	@GetMapping("/signin")
	public String signInForm(HttpServletRequest request,
			@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
			@RequestParam(value = "fromSignup", required = false) Boolean fromSignup) {
		if (fromSignup == null || !fromSignup) {
			if (redirectUrl == null) {
				redirectUrl = request.getHeader("Referer");
			}
			if (redirectUrl != null && !redirectUrl.contains("/signin") && !redirectUrl.contains("/signup")) {
				request.getSession().setAttribute("redirectUrl", redirectUrl);
			}
		}
		return "user/signin";
	}
//	@GetMapping("/signin")
//    public String signInForm(@RequestParam(required = false) String error, Model model) {
//        if (error != null && error.equals("true")) {
//            model.addAttribute("errorMessage", "1234");
//        }
//        
//        return "user/signin"; // 로그인 페이지를 반환
//    }

	@PostMapping("/signin")
	public String signIn(UserSignInDto dto, Model model, HttpSession session, HttpServletRequest request,
	                     HttpServletResponse response) {
	    log.debug("POST signIn({})", dto);


		try {
			User user = userDService.read(dto);
			if (user != null) {
				session.setMaxInactiveInterval(SESSION_TIME);
				session.setAttribute(SESSION_ATTR_USER, user.getUserid());
				session.setAttribute(SESSION_USER_GRADE, user.getGrade());
				session.setAttribute(SESSION_ATTR_USERID, user.getId());
				
	            log.info("세션 아이디 값 : {}", session.getAttribute(SESSION_ATTR_USER));
	            log.info("세션 등급 값 : {}", session.getAttribute(SESSION_USER_GRADE));
	            // 리다이렉트 URL 사용
	            String redirectUrl = (String) session.getAttribute("redirectUrl");
	            if (redirectUrl != null && !redirectUrl.contains("/signup")) {
	                session.removeAttribute("redirectUrl");
	                return "redirect:" + redirectUrl;
	            }
	            return "redirect:/";  // 기본적으로 홈페이지로 리다이렉트
	        } else {
	            model.addAttribute("errorMessage", "일치하는 아이디와 비밀번호가 없습니다.");
	            return "user/signin";
	        }
	    } catch (Exception e) {
	        log.error("로그인 처리 중 오류 발생", e);
	        return "redirect:/user/signin";
	    }
	}


	@GetMapping("/signup") // GET 방식의 /user/signup 요청을 처리하는 컨트롤러 메서드
	public void signUp() {
		log.info("GET signUp()");
	}

	@PostMapping("/signup")
	public String signUp(@RequestParam String userType,
			normalUserCreateDto normalDto, expertUserCreateDto expertDto,
			RedirectAttributes redirectAttributes) throws Exception {
		log.info("POST signUp()");
		if (userType.equals("일반회원")) {
			userDService.nomalUserCreate(normalDto);
			redirectAttributes.addFlashAttribute("signupSuccess", "normal");
		} else if (userType.equals("전문가")) {
			userDService.expertUserCreate(expertDto);
			redirectAttributes.addFlashAttribute("signupSuccess", "expert");
		}

		return "redirect:/user/signin";
	}

	@GetMapping("checkUserid")
	@ResponseBody
	public ResponseEntity<String> checkUserid(@RequestParam String userid) {
		log.info("checkUserid(userid={})", userid);

		boolean result = userService.checkUserid(userid);
		
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@GetMapping("checkNickname")
	@ResponseBody
	public ResponseEntity<String> checkNickname(@RequestParam String nickname) {
		log.info("checkNickname(nickname={})", nickname);

		boolean result = userService.checkNickname(nickname);
		
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@GetMapping("checkEmail")
	@ResponseBody
	public ResponseEntity<String> checkEmail(@RequestParam String email) {
		log.info("checkEmail(email={})", email);

		boolean result = userService.checkEmail(email);
		
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@GetMapping("checkPhone")
	@ResponseBody
	public ResponseEntity<String> checkPhone(@RequestParam String phone) {
		log.info("checkPhone(phone={})", phone);

		boolean result = userService.checkPhone(phone);
		
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@GetMapping("checkAccept")
	@ResponseBody
	public ResponseEntity<String> checkAccept(@RequestParam String accept) {
		log.info("checkAccept(accept={})", accept);

		boolean result = userService.checkAccept(accept);
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}
	}

	@PostMapping("/findId")
	public ResponseEntity<?> findID(@RequestBody findIdDto dto) {
		log.info("findID(dto={})", dto);

		try {
			User user = userService.findUserid(dto);

			if (user != null) {
				return ResponseEntity.ok().body(Map.of("userid", user.getUserid()));
			} else {
				// 사용자를 찾지 못한 경우
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "일치하는 사용자 정보를 찾을 수 없습니다."));
			}
		} catch (Exception e) {
			// 예외 발생 시 처리
			log.error("아이디 찾기 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "서버 오류가 발생했습니다. 나중에 다시 시도해주세요."));
		}
	}

	@PostMapping("/findPw")
	public ResponseEntity<String> findPw(@RequestBody findPasswordDto dto) {
		log.info("findPw(dto={})", dto);

		boolean result = userService.findPassword(dto);
		
		if (result) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}

	}

	@PostMapping("/updatePw")
	public ResponseEntity<String> updatePw(@RequestBody UpdatePasswordDto dto) {
		log.info("updatePw(dto={})", dto);

		boolean result = userService.UpdatePassword(dto);
		
		if (result == true) {
			return ResponseEntity.ok("Y");
		} else {
			return ResponseEntity.ok("N");
		}

	}

	@GetMapping("/signout")
	public String signOut(HttpSession session) {
		log.info("signOut()");
		
		session.removeAttribute(SESSION_ATTR_USER);
		session.removeAttribute(SESSION_USER_GRADE);
		session.invalidate();

		return "redirect:/user/signin";
	}

	@GetMapping("/exchange")
	public String exchange(Model model, HttpSession session) {
		Object useridObj = session.getAttribute(SESSION_ATTR_USER);

		String userid = useridObj.toString();
		log.info("User ID from session: {}", userid);

		exchangeInfoDto dto = userService.exchangeInfo(userid);

		model.addAttribute("userPoint", dto.getPoint());
		model.addAttribute("userAccount", dto.getAccount());
		model.addAttribute("userWithdraw", dto.getWithdraw());
		return "/user/exchange";
	}

	@PostMapping("/exchange")
	public ResponseEntity<String> updatePoint(@RequestBody UpdatePointDto dto, HttpSession session) {
		Object useridObj = session.getAttribute(SESSION_ATTR_USER);

		String userid = useridObj.toString();
		log.info("sessionId: {}", userid);

		exchangeInfoDto edto = userService.exchangeInfo(userid);
		long currentPoints = edto.getPoint();

		// 출금 요청 금액 검사
		if (dto.getPoint() > currentPoints) {
			return ResponseEntity.ok("P");
		}

		try {
			int result = userService.UpdatePoint(userid, dto);
			
			if (result == 1) {
				return ResponseEntity.ok("Y");
			} else {
				return ResponseEntity.ok("N");
			}
		} catch (Exception e) {
			return ResponseEntity.ok("N");
		}
	}

}
