package com.itwill.golfro.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.dto.CommentCreateDto;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.ReviewPostCreateDto;
import com.itwill.golfro.dto.ReviewPostListDto;
import com.itwill.golfro.dto.ReviewPostSearchDto;
import com.itwill.golfro.dto.ReviewPostUpdateDto;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.service.MediaService;
import com.itwill.golfro.service.ReviewPostService;
import com.itwill.golfro.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {

	public static final String SESSION_ATTR_USER = "signedInUser";
	
	private final CommentRepository cmtRepo;
	private final ReviewPostService reviewPostService;
	private final MediaService mediaService;
	private final UserService userService;

	private Map<String, Set<Integer>> userLikedPosts = new HashMap<>();
	
	@ModelAttribute("loggedInUser")
	public User addLoggedInUserToModel(HttpSession session) {
		String userid = SESSION_ATTR_USER;
		return reviewPostService.getLoggedInUser(userid);
	}

	@GetMapping("/review_create")
	public String createReviewPost(@ModelAttribute("loggedInUser") User loggedInUser, Model model) {
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
		}
		return "review/review_create";
	}

	@PostMapping("/review_create")
	public String create(@ModelAttribute ReviewPostCreateDto dto,
			@RequestParam(value = "media", required = false) MultipartFile mediaFile,
			@ModelAttribute("loggedInUser") User loggedInUser) {
		if (loggedInUser != null) {
			log.info("create(loggedInUser={})", loggedInUser);
		}

		if (mediaFile != null && !mediaFile.isEmpty()) {
			String fileName = mediaService.storeFile(mediaFile);
			dto.setMediaPath(fileName);
		}

		reviewPostService.Create(dto);
		return "redirect:/review/review_main";
	}

	@GetMapping("/review_main")
	public String viewreviewMain(@RequestParam(name = "search-category", required = false) String category,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		log.info("GET: viewreviewMain(category={}, keyword={}, pageNo={})", category, keyword, pageNo);

		Page<ReviewPostListDto> posts;
		List<Post> pinnedPosts = reviewPostService.Fixingthetop();
		List<Long> pinnedPostIds = pinnedPosts.stream().map(Post::getId).collect(Collectors.toList());

		if (category != null && keyword != null && !keyword.isEmpty()) {
			ReviewPostSearchDto searchDto = new ReviewPostSearchDto();
			searchDto.setCategory(category);
			searchDto.setKeyword(keyword);
			posts = reviewPostService.search(searchDto, pageNo, Sort.by("id").descending());
		} else {
			posts = reviewPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		}

		List<ReviewPostListDto> filteredPostsList = posts.getContent().stream()
				.filter(post -> !pinnedPostIds.contains(post.getId()))
	            .collect(Collectors.toList());

		posts = new PageImpl<>(filteredPostsList, posts.getPageable(), posts.getTotalElements() - pinnedPosts.size());
		
		model.addAttribute("posts", posts);
		model.addAttribute("pinnedPosts", pinnedPosts);

		// 카테고리 매핑 정보 추가하기
		Map<String, String> categoryMap = new HashMap<>();
		categoryMap.put("P004", "리뷰");

		Map<String, String> userNicknames = userService.getUserNicknames();

		model.addAttribute("userNicknames", userNicknames);
		model.addAttribute("posts", posts);
		model.addAttribute("categoryMap", categoryMap);
		model.addAttribute("keyword", keyword);

		return "review/review_main";
	}

	@GetMapping("/review_details")
	public String detailsCommunityPost(@ModelAttribute("loggedInUser") User loggedInUser,
			@RequestParam("id") long id, @RequestParam(name = "commentId", required = false) long commentId,
			Model model, HttpSession session) {
		if (loggedInUser != null) {
			log.info("detailsCommunityPost(loggedInUser={})", loggedInUser);
			model.addAttribute("user", loggedInUser);

			@SuppressWarnings("unchecked")
			Set<Long> viewedPosts = (Set<Long>) session.getAttribute("viewedPosts");
			if (viewedPosts == null) {
				viewedPosts = new HashSet<>();
			}

			if (!viewedPosts.contains(id)) {
				reviewPostService.increaseViews(id); // 조회수 증가
				viewedPosts.add(id);
				session.setAttribute("viewedPosts", viewedPosts);
			} else {
				log.info("이미 조회한 게시물입니다.");
			}
		} else {
			log.info("로그인하지 않은 사용자는 조회수가 증가하지 않습니다.");
		}

		// 게시물 조회
		Post post = reviewPostService.read(id);

		String[] category = {"P004"};
		
		// 이전 글과 다음 글 찾기
		Post previousPost = reviewPostService.getPreviousPost(category, post.getCreatedTime());
		Post nextPost = reviewPostService.getNextPost(category, post.getCreatedTime());

		// 댓글 목록 조회
		List<Comment> commentlist = reviewPostService.readAllComment(id);
		long commentcount = reviewPostService.selectCommentCount(id);

		Map<String, String> userNicknames = userService.getUserNicknames();

		// 모델에 속성 추가
		model.addAttribute("userNicknames", userNicknames);
		model.addAttribute("post", post); // 불러온 게시물 속성 추가
		model.addAttribute("previousPost", previousPost); // 이전 글
		model.addAttribute("nextPost", nextPost); // 다음 글
		model.addAttribute("commentlist", commentlist); // 댓글 목록 추가하기
		model.addAttribute("commentcount", commentcount);
		model.addAttribute("commentId", commentId);
		
		return "review/review_details";
	}

	@GetMapping("/review_modify")
	public String modifyReviewPost(@RequestParam("id") long id, Model model) {
		log.info("modifyReviewPost(id={})", id);
		
		Post post = reviewPostService.read(id);

		// 모델에 속성 추가
		model.addAttribute("post", post);

		return "review/review_modify";
	}

	@PostMapping("/update")
	public String update(ReviewPostUpdateDto dto) {
		log.info("update(dto={})", dto);

		// 서비스 컴포넌트의 메서드를 호출해서 데이터베이스 테이블 업데이트를 수행.
		reviewPostService.update(dto);

		return "redirect:/review/review_details?id=" + dto.getId();
	}

	@GetMapping("/delete")
	public String delete(@RequestParam(name = "id") int id) {
		log.info("delete(id={})", id);

		reviewPostService.delete(id);

		return "redirect:/review/review_main";
	}

	@PostMapping("/increaseLikes")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> increaseLikes(@RequestParam("id") long id, HttpSession session) {
		log.info("increaseLikes(id={})", id);
		
		String userid = SESSION_ATTR_USER;

		// 해당 사용자가 이미 좋아요를 눌렀는지 확인
		Set<Integer> likedPosts = userLikedPosts.getOrDefault(userid, new HashSet<>());
		if (likedPosts.contains((int) id)) {
			// 이미 좋아요를 누른 경우 예외 처리
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "이미 좋아요를 눌렀습니다."));
		}

		// 좋아요를 증가시키고, 사용자의 좋아요 목록에 추가
		reviewPostService.increaseLikes(id);
		likedPosts.add((int) id);
		userLikedPosts.put(userid, likedPosts);

		// 업데이트된 좋아요 개수 반환
		Post updatedPost = reviewPostService.read(id);
		Map<String, Object> response = new HashMap<>();
		response.put("likes", updatedPost.getLikes());
		
		return ResponseEntity.ok(response);
	}

	// 댓글 목록 조회
	@GetMapping("/comments/{postId}")
	public String getCommentsByPostId(@PathVariable long postId, Model model) {
		List<Comment> comments = cmtRepo.selectByPostId(postId);
		Map<String, String> userNicknames = userService.getUserNicknames();
		
		model.addAttribute("comments", comments);
		model.addAttribute("userNicknames", userNicknames);	

		return "review/review_details";
	}

	@PostMapping("/comments")
	@ResponseBody
	public Comment addComment(@RequestBody CommentCreateDto commentCreateDto, @ModelAttribute("loggedInUser") User loggedInUser) {
		if (loggedInUser != null) {
			Comment comment = Comment.builder()
					.user(User.builder().userid(loggedInUser.getUserid()).build())
					.post(Post.builder().id(commentCreateDto.getPostId()).build())
					.content(commentCreateDto.getContent())
					.build(); // 댓글 작성자 설정

			// 댓글을 데이터베이스에 저장하고 자동 생성된 id를 받아옴
			Comment result = cmtRepo.save(comment);

			// 저장된 댓글 객체를 반환하여 클라이언트에게 전달
			if (result != null) {
				return comment;
			} else {
				// 저장에 실패한 경우 처리
				return null;
			}
		} else {
			// 로그인 되지 않은 경우 처리
			return null;
		}
	}

	@PutMapping("/comments")
	@ResponseBody
	public Comment updateComment(@RequestBody CommentUpdateDto commentUpdateDto) {
		Comment comment = cmtRepo.selectCommentById(commentUpdateDto.getId());
		if (comment != null) {
			comment.update(commentUpdateDto.getContent());
			Comment result = cmtRepo.save(comment);
			
			if (result != null) {
				return comment; // 수정된 댓글 객체 반환
			}
		}
		
		return null;
	}

	@GetMapping("/media/{fileName}")
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getMedia(@PathVariable("fileName") String fileName) {
		ByteArrayResource resource = mediaService.loadFileAsResource(fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
	}

	// DELETE 요청의 URL 수정
	@DeleteMapping("/comments/{id}")
	@ResponseBody
	public String deleteComment(@PathVariable("id") long id) {
		try {
			cmtRepo.deleteById(id);
			return "Deleted comment with id: " + id;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to delete comment with id: " + id;
		}
	}

}
