package com.itwill.golfro.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.golfro.domain.User;
import com.itwill.golfro.service.UserMypageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserHomeController {
	
	public static final String SESSION_ATTR_USER = "signedInUser";
	public static final String SESSION_USER_GRADE = "signedInUserGrade";
	private final UserMypageService userService;

	@GetMapping("/mypage")
	public void mypage(HttpSession session, Model model) {
		log.info("mypage()");

		Object userid = session.getAttribute(SESSION_ATTR_USER);
		Object grade = session.getAttribute(SESSION_USER_GRADE);

		if (grade.equals("G10")) {
			User pro = userService.readPro((String) userid);
			model.addAttribute("user", pro);
			log.info("user={}", pro);
		} else {
			User user = userService.read((String) userid);
			model.addAttribute("user", user);
			log.info("user={}", user);
		}
	}
	
}
