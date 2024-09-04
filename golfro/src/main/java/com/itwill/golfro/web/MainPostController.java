package com.itwill.golfro.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.itwill.golfro.domain.User;
import com.itwill.golfro.domain.Club;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.dto.MainPostCreateDto;
import com.itwill.golfro.dto.MainPostDetailsDto;
import com.itwill.golfro.dto.MainPostListDto;
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
			@RequestParam(required = false) String userid,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String keyword, HttpSession session, Model model) {
		log.debug("mainPostList(pageNo={}, userid={}, category={}, keyword={})", pageNo, userid, category, keyword);

		Page<Tuple> posts;

		String sessionUserid = SESSION_ATTR_USER;

		if (userid == null) {
			posts = mainPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		} else {
			posts = mainPostService.getPagedPostsByUserid(sessionUserid, pageNo, Sort.by("id").descending());
		}

		List<Club> clubs = mainPostService.clubTypes();

		List<MainPostListDto> dtos = posts.stream()
			    .map(tuple -> {
			        Long id = tuple.get(0, Long.class);
			        Club club = Club.builder().name(tuple.get(1, String.class)).build(); // DTO에서 'name'으로 사용
			        String title = tuple.get(2, String.class); // DTO에서 'title'으로 사용
			        User user = User.builder().nickname(tuple.get(3, String.class)).build(); // DTO에서 'nickname'으로 사용
			        Long views = tuple.get(4, Long.class); // DTO에서 'views'로 사용
			        Long likes = tuple.get(5, Long.class); // DTO에서 'likes'로 사용
			        LocalDateTime createdTime = tuple.get(6, LocalDateTime.class); // DTO에서 'createdTime'으로 사용
			        String status = tuple.get(7, String.class); // DTO에서 'status'로 사용
			        Integer selection = tuple.get(8, Integer.class);
			        
			        
			        // LocalDateTime을 포맷팅하여 String으로 변환
			        String formattedCreatedTime = createdTime != null ? formatDateTime(createdTime) : null;
			        
			        // MainPostListDto 객체 생성
			        return MainPostListDto.builder()
			            .id(id)
			            .club(club)
			            .title(title)
			            .user(user)
			            .views(views)
			            .likes(likes)
			            .createdTime(formattedCreatedTime)
			            .status(status)
			            .selection(selection)
			            .build();
			    })
			    .collect(Collectors.toList());
		
		model.addAttribute("posts", dtos);
		model.addAttribute("clubs", clubs);

		if (userid != null) {
			return "/user/myLessonList"; // 사용자가 로그인한 상태에서는 다른 뷰로 이동
		}

		return "mainPost/list"; // 그 외의 경우는 메인 게시글 리스트 뷰로 이동
	}

	@GetMapping("/details/{id}")
	public String mainPostDetails(@PathVariable(name = "id") long id,
		Model model, HttpSession session, RedirectAttributes redirectAttributes
		, @RequestParam(required = false) Long commentId ) {
		log.debug("mainPostDetails(id={})", id);
		
		
		if (session.getAttribute(SESSION_ATTR_USER) != null) {
		    @SuppressWarnings("unchecked")
		    Set<Long> viewedPosts = (Set<Long>) session.getAttribute("viewedPosts");
		    if (viewedPosts == null) {
		        viewedPosts = new HashSet<>();
		        session.setAttribute("viewedPosts", viewedPosts);
		    }

		    // 게시글이 세션에 없으면 조회수를 증가시키고 세션에 추가
		    if (!viewedPosts.contains(id)) {
		        mainPostService.increaseViews(id);
		        viewedPosts.add(id);
		    }
		}
		

		// 로그인한 사용자인 경우, 기존 로직 수행
		MainPostDetailsDto post = mainPostService.selectPostId(id);
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
		MainPostDetailsDto post = mainPostService.selectPostId(id);
		
		model.addAttribute("clubs", clubs);
		model.addAttribute("post", post);
	}

	@GetMapping("/video")
	@ResponseBody
	public Resource test(@RequestParam String file) throws IOException {
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

		return "redirect:/mainPost/details/" + dto.getId();
	}
	
	

	@GetMapping("/delete")
	public String deleteMainPost(@RequestParam(name="id") long id) {
		log.debug("deleteMainPost(id={})", id);

		mainPostService.deleteById(id);
		
		return "redirect:/mainPost/list";
	}

	@PutMapping("/likes/{id}")
	public ResponseEntity<Void> updateLikes(@PathVariable long id) {
		log.debug("updateLikes(id={})", id);
		
		mainPostService.updatePostLikes(id);
		
		return ResponseEntity.ok().build();
	}

	@GetMapping("/likes/{id}")
	@ResponseBody
	public long getLikes(@PathVariable long id) {
		log.debug("getLikes(id={})", id);
		return mainPostService.getPostLikes(id);
	}

	@GetMapping("/search")
	public String searchPosts(MainPostSearchDto dto, MyPostSearchDto myDto,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		log.info("searchPosts(dto={}, myDto={}) by userid", dto, myDto);
		
		Page<Post> posts;
		List<Club> clubs = mainPostService.clubTypes();

		if (myDto != null && myDto.getUserid() != null && !myDto.getUserid().isEmpty()) {
			posts = mainPostService.searchReadByUserid(myDto, pageNo, Sort.by("id").descending());
			model.addAttribute("userid", myDto.getUserid()); // 해당 사용자의 ID를 뷰로 전달
		} else {
			posts = mainPostService.searchRead(dto, pageNo, Sort.by("id").descending());
		}
//		log.info("여기야아아아아ㅏ아아아아아아아아" + posts.getSize());
		
		model.addAttribute("posts", posts);
		model.addAttribute("clubs", clubs);

		return "mainPost/list";
	}
	
	private static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            // UTC 시간대로 변환
            ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("UTC"));
            // 한국 시간대로 변환
            ZonedDateTime seoulDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
            // 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 포맷 적용
            return seoulDateTime.format(formatter);
        } else {
            return null;
        }
	}
}
