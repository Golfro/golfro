package com.itwill.golfro.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.JoinPostCreateDto;
import com.itwill.golfro.dto.JoinPostSearchDto;
import com.itwill.golfro.dto.JoinPostUpdateDto;
import com.itwill.golfro.service.JoinPostService;
import com.itwill.golfro.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/join")
public class JoinController {

	public static final String SESSION_ATTR_USER = "signedInUser";
	
	private final JoinPostService joinPostService;
	private final UserService userService;

	@ModelAttribute("loggedInUser")
	public User addLoggedInUserToModel(HttpSession session) {
		String userid = SESSION_ATTR_USER;
		return joinPostService.getLoggedInUser(userid);
	}

	@GetMapping("/join_create")
	public void createJoinPost(User loggedInUser, Model model) {
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
		}
	}

	@PostMapping("/join_create")
	public String create(JoinPostCreateDto dto) {
		log.info("create(dto={})", dto);
		
		joinPostService.create(dto);
		
		return "redirect:/join/join_main";
	}

	@GetMapping("/join_main")
	public String viewJoinMain(@RequestParam(value = "teeoffDate", required = false) String teeoffDateStr,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String keyword,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		Page<Post> posts;
		
		LocalDate today = LocalDate.now();
		List<LocalDate> dates = new ArrayList<>();
		
		// 오늘부터 5일간의 날짜 목록 생성
		for (int i = 0; i < 5; i++) {
			dates.add(today.plusDays(i));
		}

		// 검색 처리
		if ((category != null && !category.isEmpty()) && (keyword != null && !keyword.isEmpty())) {
			JoinPostSearchDto searchDto = new JoinPostSearchDto(category, keyword);
			posts = joinPostService.search(searchDto, pageNo, Sort.by("id").descending());
		} else if (teeoffDateStr != null && !teeoffDateStr.isEmpty()) {
			try {
				LocalDate teeoffDate = LocalDate.parse(teeoffDateStr);
				posts = joinPostService.findByTeeoffDate(teeoffDate.atStartOfDay(), pageNo, Sort.by("id").descending());
			} catch (DateTimeParseException e) {
				log.error("날짜 형식 오류: {}", teeoffDateStr, e);
				posts = joinPostService.read(pageNo, Sort.by("id").descending());
			}
		} else {
			posts = joinPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		}

		Map<String, String> userNicknames = userService.getUserNicknames();

		model.addAttribute("userNicknames", userNicknames);
		model.addAttribute("posts", posts);
		model.addAttribute("selectedCategory", category); // 선택된 카테고리를 모델에 추가
		model.addAttribute("keyword", keyword); // 검색어를 모델에 추가
		model.addAttribute("today", LocalDateTime.now()); // 오늘 날짜를 모델에 추가

		return "join/join_main";
	}

	@GetMapping({ "/join_details", "/join_modify" })
	public void detailsjoinPost(@RequestParam long id, @ModelAttribute User loggedInUser, Model model) {
		if (loggedInUser != null) {
			log.info("user={}", loggedInUser);
			model.addAttribute("user", loggedInUser);
		}

		Map<String, String> userNicknames = userService.getUserNicknames();

		model.addAttribute("userNicknames", userNicknames);
		Post post = joinPostService.read(id);
		
		String[] category = {"P003"};
		
		Post previousPost = joinPostService.getPreviousPost(category, post.getTeeoff());
		Post nextPost = joinPostService.getNextPost(category, post.getTeeoff());
		model.addAttribute("post", post);
		model.addAttribute("previousPost", previousPost);
		model.addAttribute("nextPost", nextPost);
	}

	@PostMapping("/update")
	public String update(JoinPostUpdateDto dto) {
		log.info("update(dto={})", dto);
		
		joinPostService.update(dto);
		
		return "redirect:/join/join_details?id=" + dto.getId();
	}

	@GetMapping("/delete")
	public String delete(@RequestParam long id) {
		log.info("delete(id={})", id);
		
		joinPostService.delete(id);
		
		return "redirect:/join/join_main";
	}
	
}
