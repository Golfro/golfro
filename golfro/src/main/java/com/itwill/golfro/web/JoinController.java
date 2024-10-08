package com.itwill.golfro.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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
			@RequestParam(required = false) Integer holes,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		Page<Post> posts;
		Sort sort = Sort.by("teeoff").descending(); // 정렬 설정
		
		LocalDate today = LocalDate.now();
		List<LocalDate> dates = new ArrayList<>();
		
		// 오늘부터 5일간의 날짜 목록 생성
		for (int i = 0; i < 5; i++) {
			dates.add(today.plusDays(i));
		}

		// 검색 처리
		if ((category != null && !category.isEmpty()) && (keyword != null && !keyword.isEmpty())) {
			JoinPostSearchDto searchDto = new JoinPostSearchDto(category, keyword);
			posts = joinPostService.search(searchDto, pageNo, sort);
		} else if ((category != null && !category.isEmpty()) && (holes != null)) {
			JoinPostSearchDto searchDto = new JoinPostSearchDto(category, holes.toString());
			posts = joinPostService.search(searchDto, pageNo, sort);
		} else if (teeoffDateStr != null && !teeoffDateStr.isEmpty()) {
			try {
				LocalDate teeoffDate = LocalDate.parse(teeoffDateStr);
				posts = joinPostService.findByTeeoffDate(teeoffDate, pageNo, sort);
			} catch (DateTimeParseException e) {
				log.error("날짜 형식 오류: {}", teeoffDateStr, e);
				posts = joinPostService.read(pageNo, sort);
			}
		} else {
			posts = joinPostService.getPagedPosts(pageNo, sort);
		}

		model.addAttribute("posts", posts);
		model.addAttribute("selectedCategory", category); // 선택된 카테고리를 모델에 추가
		model.addAttribute("keyword", keyword); // 검색어를 모델에 추가
		model.addAttribute("today", LocalDateTime.now()); // 오늘 날짜를 모델에 추가

		return "join/join_main";
	}

	@GetMapping({ "/join_details", "/join_modify" })
	public void detailsjoinPost(@RequestParam long id, Model model) {
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
