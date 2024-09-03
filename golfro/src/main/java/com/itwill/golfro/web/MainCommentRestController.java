package com.itwill.golfro.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.dto.CommentUpdateDto;
import com.itwill.golfro.dto.MainCommentCreateDto;
import com.itwill.golfro.dto.MainCommentItemDto;
import com.itwill.golfro.service.MainCommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/mainPost/api/mainComment")
@RestController
public class MainCommentRestController {

	@Autowired
	private MainCommentService mainCommentService;

	@PostMapping
	public ResponseEntity<Integer> regisgerMainComment(@RequestBody MainCommentCreateDto dto) {
		log.debug("registerComment(dto={})", dto);

		int result = mainCommentService.mainPostCommentCreate(dto);
		return ResponseEntity.ok(result);
	}
	

	// postId에 작성된 모든 Comments를 읽는 메서
	@GetMapping("/all/{postId}")
	public ResponseEntity<List<MainCommentItemDto>> getMainCommentByPostId(@PathVariable(name = "postId") Long postId) {
		log.debug("getMainCommentByPostId(postId={})", postId);

		List<MainCommentItemDto> list = mainCommentService.commentReadByPostId(postId);
		
		return ResponseEntity.ok(list);
	}
	

	@PutMapping("/selectComment/{id}")
	public ResponseEntity<?> selectionComment(@PathVariable(name = "id") int id) {
		log.debug("selectionComment(id={})", id);
		try {
			mainCommentService.selectCommentAndGiftPoint(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("Error updating comment or gifting points", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Integer> deleteComment(@PathVariable(name = "id") int id) {
		log.debug("deleteComment(id={})", id);
		
		int result = mainCommentService.deleteComment(id);
		
		return ResponseEntity.ok(result);

	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<Integer> editComment(@RequestBody CommentUpdateDto dto) {
		log.debug("editComment(dto={})", dto);
		
		int result = mainCommentService.editCommentById(dto);
		
		return ResponseEntity.ok(result);
	}

}
