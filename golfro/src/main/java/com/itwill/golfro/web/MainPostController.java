package com.itwill.golfro.web;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.golfro.domain.Club;
import com.itwill.golfro.dto.MainPostCreateDto;
import com.itwill.golfro.dto.MainPostSearchDto;
import com.itwill.golfro.dto.MainPostUpdateDto;
import com.itwill.golfro.dto.MyPostSearchDto;
import com.itwill.golfro.service.MainPostService;
import com.querydsl.core.Tuple;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mainPost")
public class MainPostController {
	
	public static final String SESSION_ATTR_USER = "signedInUser";
	
	private final MainPostService mainPostService;

	@GetMapping("/create")
	public void mainPostCreate(Model model) {
		List<Club> clubs = mainPostService.clubTypes();
		model.addAttribute("clubs", clubs);
	}

	@PostMapping("/create")
	public String mainPostCreate(@ModelAttribute MainPostCreateDto dto) {
		log.debug("POST: mainPostCreate(dto={})", dto);
		log.debug("file={}", dto.getMedia());
		
		mainPostService.mainCreate(dto);
		
		return "redirect:/mainPost/list";
	}

	@GetMapping("/list")
	public String mainPostList(@RequestParam(name = "p", defaultValue = "0") int pageNo,
			@RequestParam(name = "userid", required = false) String userid,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "keyword", required = false) String keyword, HttpSession session, Model model) {
		log.debug("mainPostList(pageNo={}, userid={}, category={}, keyword={})", pageNo, userid, category, keyword);

		Page<Tuple> posts;

		String sessionUserid = SESSION_ATTR_USER;

		if (userid == null) {
			posts = mainPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		} else {
			posts = mainPostService.getPagedPostsByUserid(sessionUserid, pageNo, Sort.by("id").descending());
		}

		List<Club> clubs = mainPostService.clubTypes();

		model.addAttribute("post", posts);
		model.addAttribute("clubs", clubs);

		if (userid != null) {
			return "/user/myLessonList"; // 사용자가 로그인한 상태에서는 다른 뷰로 이동
		}

		return "/mainPost/list"; // 그 외의 경우는 메인 게시글 리스트 뷰로 이동
	}

	@GetMapping("/details")
	public String mainPostDetails(@RequestParam(name = "id") long id,
			@RequestParam(name = "commentId", required = false) long commentId,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		log.debug("mainPostDetails(id={}, commentId={})", id, commentId);

		// 로그인한 사용자인 경우, 기존 로직 수행
		Tuple post = mainPostService.selectPostId(id);
		log.debug("post={}", post);
		
		model.addAttribute("commentId", commentId);
		model.addAttribute("post", post);

		// 뷰 이름 반환
		return "/mainPost/details"; // 또는 적절한 뷰 이름
	}

	@GetMapping("/modify")
	public void mainPostModify(@RequestParam(name = "id") long id, Model model) {
		log.debug("mainPostModify(id={})", id);
		
		List<Club> clubs = mainPostService.clubTypes();
		Tuple post = mainPostService.selectPostId(id);
		
		model.addAttribute("clubs", clubs);
		model.addAttribute("post", post);
	}

	@GetMapping("/video")
	@ResponseBody
	public Resource test(@RequestParam(name = "file") String file) throws IOException {
		log.info("test(file={})", file);

		Path path = Paths.get(file);
		log.info("path={}", path);

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return resource;
	}

	@PostMapping("/update")
	public String mainPostUpdate(MainPostUpdateDto dto) {
		log.debug("mainPostUpdate(dto={})", dto);
		
		mainPostService.mainPostUpdate(dto);

		return "redirect:/mainPost/details?id=" + dto.getId();
	}

	@GetMapping("/delete")
	public String deleteMainPost(@RequestParam(name = "id") long id) {
		log.debug("deleteMainPost(id={})", id);

		mainPostService.deleteById(id);
		
		return "redirect:/mainPost/list";
	}

	@PutMapping("/likes/{id}")
	public ResponseEntity<Void> updateLikes(@PathVariable(name = "id") long id) {
		log.debug("updateLikes(id={})", id);
		
		mainPostService.updatePostLikes(id);
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("/likes/{id}")
	@ResponseBody
	public long getLikes(@PathVariable(name = "id") long id) {
		log.debug("getLikes(id={})", id);
		return mainPostService.getPostLikes(id);
	}

	@GetMapping("/search")
	public String searchPosts(MainPostSearchDto dto, MyPostSearchDto myDto,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		log.debug("searchPosts(dto={}, myDto={}) by userid");
		
		Page<Tuple> posts;
		List<Club> clubs = mainPostService.clubTypes();

		if (myDto != null && myDto.getUserid() != null && !myDto.getUserid().isEmpty()) {
			posts = mainPostService.searchReadByUserid(myDto, pageNo, Sort.by("id").descending());
			model.addAttribute("userid", myDto.getUserid()); // 해당 사용자의 ID를 뷰로 전달
		} else {
			posts = mainPostService.searchRead(dto, pageNo, Sort.by("id").descending());
		}

		model.addAttribute("post", posts);
		model.addAttribute("clubs", clubs);

		return "/mainPost/list";
	}
	
}
