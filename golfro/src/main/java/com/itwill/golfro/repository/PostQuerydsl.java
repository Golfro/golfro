package com.itwill.golfro.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.dto.CommPostSearchDto;
import com.itwill.golfro.dto.JoinPostSearchDto;
import com.itwill.golfro.dto.MainPostSearchDto;
import com.itwill.golfro.dto.MyPostListSearchDto;
import com.itwill.golfro.dto.MyPostSearchDto;
import com.itwill.golfro.dto.ReviewPostSearchDto;
import com.querydsl.core.Tuple;

public interface PostQuerydsl {
	Post findPreviousPost(String[] category, LocalDateTime teeoff);
	
	Post findNextPost(String[] category, LocalDateTime teeoff);
	
	
	Post findPreviousPostComm(String[] category, LocalDateTime createdTime);
	
	Post findNextPostComm(String[] category, LocalDateTime createdTime);
	
	
	
	
	Post selectById(Long id);
	
	Tuple selectByPostId(Long id);
	
	Page<Post> selectOrderByIdDesc(String[] categories, Pageable pageable);
	
	List<Post> selectTop5ByF001();
	
	List<Post> selectTop5ByF002();
	
	List<Post> Fixingthetop();
	
	List<Post> Fixingthetop2();
	
	Page<Post> selectByCategoryAndKeyword(CommPostSearchDto dto, Pageable pageable);
	
	List<Post> selectOrderByTeeoffDesc();
	
	Page<Post> search(JoinPostSearchDto dto, Pageable pageable);
	
	Page<Post> selectByTeeoffDate(LocalDate teeoffDate, Pageable pageable);
	
	List<Tuple> selectReadAll();
	
	List<Tuple> selectReadAllByUserid(String userid);
	
	Page<Post> search(MainPostSearchDto dto, Pageable pageable);
	
	Page<Post> searchMyPost(MyPostSearchDto dto, Pageable pageable);
	
	Page<Tuple> getPostList(Pageable pageable);
	
	Page<Tuple> getPostListByUserid(String userid, Pageable pageable);
	
	List<Post> getUsersLikesRank();
	
	List<Post> getCommRank();
	
	Page<Post> selectPagedPostsComm(Pageable pageable);
	
	Page<Post> selectPagedPosts(Pageable pageable);
	
	Page<Post> selectPagedPostsReview(Pageable pageable);
	
	Page<Post> selectPagedPosts(String userid, Pageable pageable);
	
	Page<Post> search(MyPostListSearchDto dto, Pageable pageable);
	
	Page<Post> search(ReviewPostSearchDto dto, Pageable pageable);
	
	Page<Post> selectPagedP004Posts(Pageable pageable); // origin: reviewpost-mapper -> id='selectPagedPosts'
	
	long selectTotalPostCount(String[] category);
	
	long getTotalCountByUserid(String userid);

	long selectLikes(Long id);
	
	long getTotalCount();
	
	long selectTotalMyPostCount(String userid); // origin: mypost-mapper -> id='selectTotalPostCount'
}