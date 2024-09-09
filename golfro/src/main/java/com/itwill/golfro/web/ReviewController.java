package com.itwill.golfro.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	private Map<String, Set<Integer>> userLikedPosts = new HashMap<>();

	
	
	@GetMapping("/review_create")
	public String createReviewPost() {

		return "review/review_create";
	}

	@PostMapping("/review_create")
	public String create(ReviewPostCreateDto dto) {

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
			model.addAttribute("keyword", keyword);
		} else {
			posts = reviewPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		}

		List<ReviewPostListDto> filteredPostsList = posts.getContent().stream()
				.filter(post -> !pinnedPostIds.contains(post.getId())).collect(Collectors.toList());

		posts = new PageImpl<>(filteredPostsList, posts.getPageable(), posts.getTotalElements() - pinnedPosts.size());

		model.addAttribute("posts", posts);
		model.addAttribute("pinnedPosts", pinnedPosts);

		return "review/review_main";
	}

	@GetMapping("/review_details")
	public String detailsCommunityPost(@RequestParam("id") long id, Model model, HttpSession session) {

		@SuppressWarnings("unchecked")
		Set<Long> viewedPosts = (Set<Long>) session.getAttribute("viewedPosts");
		if (viewedPosts == null) {
			viewedPosts = new HashSet<>();
		}

		reviewPostService.increaseViews(id); // 조회수 증가
		viewedPosts.add(id);
		session.setAttribute("viewedPosts", viewedPosts);
		log.info("이미 조회한 게시물입니다.");

		// 게시물 조회
		Post post = reviewPostService.read(id);

		String[] category = {"P004"};

		// 이전 글과 다음 글 찾기
		Post previousPost = reviewPostService.getPreviousPost(category, post.getCreatedTime());
		Post nextPost = reviewPostService.getNextPost(category, post.getCreatedTime());

		// 댓글 목록 조회
		List<Comment> commentlist = reviewPostService.readAllComment(id);
		long commentcount = reviewPostService.selectCommentCount(id);

		// 모델에 속성 추가
		model.addAttribute("post", post); // 불러온 게시물 속성 추가
		model.addAttribute("previousPost", previousPost); // 이전 글
		model.addAttribute("nextPost", nextPost); // 다음 글
		model.addAttribute("commentlist", commentlist); // 댓글 목록 추가하기
		model.addAttribute("commentcount", commentcount);

		return "review/review_details";
	}

	@GetMapping("/review_modify")
	public String modifyReviewPost(@RequestParam("id") Long id, Model model) {
		log.info("modifyReviewPost(id={})", id);

		Post post = reviewPostService.read(id);

		// 모델에 속성 추가
		model.addAttribute("post", post);

		return "review/review_modify";
	}

	@PostMapping("/update")
	public String update(ReviewPostUpdateDto dto) {
		log.info("update(dto={})", dto);

		// 서비스 컴포넌트의 메서드를 호출해서 데이터베이스 테이블 업데이트를수행.
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

	@PostMapping("/comment_add")
	@ResponseBody
	public ResponseEntity<Comment> addComment(@RequestBody CommentCreateDto commentCreateDto) {
		log.info("************={}",commentCreateDto);
		
		   
        Comment comment = Comment.builder()
                .user(User.builder().id(commentCreateDto.getUserid()).build())
                .post(Post.builder().id(commentCreateDto.getPostId()).build())
                .content(commentCreateDto.getContent())
                .build();
        log.info("여기까지 되는지 확인중");
        
        Comment result = cmtRepo.save(comment);
        log.info("리절트리절트리절트리절트리절트={}",result);
        
        return ResponseEntity.ok(result);
	}

	@PutMapping("/comments")
	@ResponseBody
	public ResponseEntity<Comment> updateComment(@RequestBody CommentUpdateDto dto) {
		log.info("************={}",dto);
		Comment comment = cmtRepo.selectCommentById(dto.getCommentId());
		if (comment != null) {
			comment.update(dto.getContent());
			cmtRepo.save(comment);
		}

		return ResponseEntity.ok(comment);
	}

	// DELETE 요청의 URL 수정
	@DeleteMapping("/comments/{commentId}")
	@ResponseBody
	public String deleteComment(@PathVariable(name="commentId") Long id) {
		try {
			cmtRepo.deleteById(id);
			return "Deleted comment with id: " + id;
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to delete comment with id: " + id;
		}
	}

}
