package com.itwill.golfro.web;

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
import com.itwill.golfro.dto.CommPostCreateDto;
import com.itwill.golfro.dto.CommPostListDto;
import com.itwill.golfro.dto.CommPostSearchDto;
import com.itwill.golfro.dto.CommPostUpdateDto;
import com.itwill.golfro.dto.CommentCreateDto;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.repository.CommentRepository;
import com.itwill.golfro.service.CommPostService;
import com.itwill.golfro.service.MediaService;
import com.itwill.golfro.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

	public static final String SESSION_ATTR_USER = "signedInUser";
	
	private final CommentRepository cmtRepo;
	private final CommPostService commPostService;
	private final MediaService mediaService;
	private final UserService userService;

	private Map<String, Set<Integer>> userLikedPosts = new HashMap<>();
	
	
	
	@ModelAttribute("loggedInUser")
	public User addLoggedInUserToModel(HttpSession session) {
		String userid = SESSION_ATTR_USER;
		return commPostService.getLoggedInUser(userid);
	}

	
	
	@GetMapping("/comm_create")
	public String createCommPost(@ModelAttribute("loggedInUser") User loggedInUser, Model model) {
		if (loggedInUser != null) {
			model.addAttribute("user", loggedInUser);
		}
		
		return "community/comm_create";
	}
	
	
	
	

	@PostMapping("/comm_create")
	public String create(@ModelAttribute CommPostCreateDto dto) {

//
//		if (mediaFile != null && !mediaFile.isEmpty()) {
//			String fileName = mediaService.storeFile(mediaFile);
//			dto.setMediaPath(fileName);
//		}

		commPostService.Create(dto);
		
		return "redirect:/community/comm_main";
	}
	
	
	
	
	

	
	@GetMapping("/comm_main")
	public String viewCommunityMain(@RequestParam(name = "post-cate", required = false) String category,
			@RequestParam(name = "search-category", required = false) String searchCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "p", defaultValue = "0") int pageNo, Model model) {
		
		
		log.info("GET: viewCommunityMain(category={}, searchCategory={}, keyword={}, pageNo={})", category, searchCategory, keyword, pageNo);

		Page<CommPostListDto> posts;
		
		// 검색 조건을 넣었을 경우
		if ((category != null && !category.isEmpty()) || (searchCategory != null && keyword != null && !keyword.isEmpty())) {
			CommPostSearchDto searchDto = new CommPostSearchDto();
			searchDto.setCategoryId(category);
			searchDto.setSearchCategory(searchCategory);
			searchDto.setKeyword(keyword);
			posts = commPostService.searchByCategoryAndKeyword(searchDto, pageNo, Sort.by("id").descending());
			
		} else {
			// 검색 조건이 없을 경우
			posts = commPostService.getPagedPosts(pageNo, Sort.by("id").descending());
		}

		List<Post> pinnedPosts = commPostService.Fixingthetop();

		List<Long> pinnedPostIds = pinnedPosts.stream().map(Post::getId).collect(Collectors.toList());

		List<CommPostListDto> filteredPostsList = posts.getContent().stream()
	            .filter(post -> !pinnedPostIds.contains(post.getId()))
	            .collect(Collectors.toList());

		posts = new PageImpl<>(filteredPostsList, posts.getPageable(), posts.getTotalElements());



		model.addAttribute("pinnedPosts", pinnedPosts);
		model.addAttribute("top5ByF001", commPostService.getTop5ByF001());
		model.addAttribute("top5ByF002", commPostService.getTop5ByF002());
		
		Map<String, String> category_name = commPostService.catrgoryname(); // 카테고리 ID / Name 매핑
		Map<String, String> userNicknames = userService.getUserNicknames(); // 유저 UserId / Nickname 매핑
		
		model.addAttribute("category_name", category_name);
		model.addAttribute("userNicknames", userNicknames);
		model.addAttribute("category_name", category_name);
		model.addAttribute("selectedCategory", category);
		model.addAttribute("selectedSearchCategory", searchCategory);
		model.addAttribute("keyword", keyword);
		model.addAttribute("posts", posts);

		return "community/comm_main";
	}

	@GetMapping("/details/{id}")
	public String detailsCommunityPost(@ModelAttribute("loggedInUser") User loggedInUser,
			@PathVariable(name="id") Long id, @RequestParam(name = "commentId", required = false) Long commentId,
			Model model, HttpSession session) {
		
		
		if (session.getAttribute(SESSION_ATTR_USER) != null) {
		    @SuppressWarnings("unchecked")
		    Set<Long> viewedPosts = (Set<Long>) session.getAttribute("viewedPosts");
		    if (viewedPosts == null) {
		        viewedPosts = new HashSet<>();
		        session.setAttribute("viewedPosts", viewedPosts);
		    }

		    // 게시글이 세션에 없으면 조회수를 증가시키고 세션에 추가
		    if (!viewedPosts.contains(id)) {
		    	commPostService.increaseViews(id);
		        viewedPosts.add(id);
		    }
		}

		// 게시물 조회
		Post post = commPostService.read(id);

		// 이전 글과 다음 글 찾기
		Post previousPost = commPostService.getPreviousPost(post.getCreatedTime());
		Post nextPost = commPostService.getNextPost(post.getCreatedTime());

		// 댓글 목록 조회
		List<Comment> commentlist = commPostService.readAllComment(id);
		long commentcount = commPostService.selectCommentCount(id);

		Map<String, String> userNicknames = userService.getUserNicknames();
		Map<String, String> category_name = commPostService.catrgoryname();

		model.addAttribute("userNicknames", userNicknames);
		model.addAttribute("category_name", category_name);
		model.addAttribute("post", post); // 불러온 게시물 속성 추가
		model.addAttribute("previousPost", previousPost); // 이전 글
		model.addAttribute("nextPost", nextPost); // 다음 글
		model.addAttribute("commentlist", commentlist); // 댓글 목록 추가하기
		model.addAttribute("commentcount", commentcount);
		model.addAttribute("commentId", commentId);
		
		model.addAttribute("top5ByF001", commPostService.getTop5ByF001());
		model.addAttribute("top5ByF002", commPostService.getTop5ByF002());
		
		System.out.println(previousPost);
		System.out.println(nextPost);
		System.out.println(commentlist);
		
		return "community/comm_details";
	}

	@GetMapping("/comm_modify/{id}")
	public String modifyCommunityPost(@PathVariable Long id, Model model) {
		Post post = commPostService.read(id);

		// 모델에 속성 추가
		model.addAttribute("post", post);

		return "community/comm_modify";
	}

	@PostMapping("/update")
	public String update(CommPostUpdateDto dto) {
		log.info("update(dto={})", dto);

		// 서비스 컴포넌트의 메서드를 호출해서 데이터베이스 테이블 업데이트를 수행.
		commPostService.update(dto);

		return "redirect:/community/details/" + dto.getId();
	}
	
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(name = "id") Long id) {
		log.info("delete(id={})", id);

		commPostService.delete(id);

		return "redirect:/community/comm_main";
	}

	@PostMapping("/increaseLikes")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> increaseLikes(@RequestParam("id") long id, HttpSession session) {
		String userid = SESSION_ATTR_USER;

		// 해당 사용자가 이미 좋아요를 눌렀는지 확인
		Set<Integer> likedPosts = userLikedPosts.getOrDefault(userid, new HashSet<>());
		log.info("likedPosts={}" , likedPosts);
		
		if (likedPosts.contains((int) id)) {
			// 이미 좋아요를 누른 경우 예외 처리
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "이미 좋아요를 눌렀습니다."));
		}

		// 좋아요를 증가시키고, 사용자의 좋아요 목록에 추가
		commPostService.increaseLikes(id);
		
		likedPosts.add((int) id);
		
		userLikedPosts.put(userid, likedPosts);
		log.info("userLikedPosts={}" , userLikedPosts);

		// 업데이트된 좋아요 개수 반환
		Post updatedPost = commPostService.read(id);
		Map<String, Object> response = new HashMap<>();
		response.put("likes", updatedPost.getLikes());
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/media/{fileName}")
	@ResponseBody
	public ResponseEntity<ByteArrayResource> getMedia(@PathVariable("fileName") String fileName) {
		ByteArrayResource resource = mediaService.loadFileAsResource(fileName);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
	}

	// 댓글 목록 조회
	@GetMapping("/comments/{postId}")
	public String getCommentsByPostId(@PathVariable Long postId, Model model) {
		List<Comment> comments = cmtRepo.selectByPostId(postId);
		
		Map<String, String> userNicknames = userService.getUserNicknames();
		
		model.addAttribute("comments", comments);
		model.addAttribute("userNicknames", userNicknames);

		return "community/comm_details";
	}

	@PostMapping("/comments")
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
	@Transactional
	public ResponseEntity<Comment> updateComment(@RequestBody CommentUpdateDto commentUpdateDto) {
	    log.info("여기 실행되는거 맞지???????");
	    log.info("{}", commentUpdateDto);

	    // 댓글을 ID로 찾기
	    Comment comment = cmtRepo.selectCommentById(commentUpdateDto.getCommentId());
	    if (comment == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // 댓글 내용 업데이트
	    comment.update(commentUpdateDto.getContent());

	    // 업데이트된 댓글 저장
	    Comment updatedComment = cmtRepo.save(comment);

	    log.info("업데이트 된 것 :{}", updatedComment);

	    // 수정된 댓글 객체 반환
	    return ResponseEntity.ok(updatedComment);
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