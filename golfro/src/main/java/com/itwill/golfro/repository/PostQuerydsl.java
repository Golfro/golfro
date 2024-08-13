package com.itwill.golfro.repository;

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
	Post findPreviousPost(String[] category, LocalDateTime createdTime);
	
	Post findNextPost(String[] category, LocalDateTime createdTime);
	
	Post selectById(Long id);
	
	Tuple selectByPostId(Long id);
	
	List<Post> selectOrderByCreatedTimeDesc(); // origin: commpost-mapper -> id='selectOrderByIdDesc'
	
	List<Post> selectOrderByIdDesc();
	
	List<Post> selectTop5ByF001();
	
	List<Post> selectTop5ByF002();
	
	List<Post> Fixingthetop();
	
	List<Post> selectByCategoryAndKeyword(CommPostSearchDto dto);
	
	List<Post> selectOrderByTeeoffDesc();
	
	List<Post> search(JoinPostSearchDto dto);
	
	List<Post> selectByTeeoffDate(LocalDateTime teeoffDate);
	
	List<Tuple> selectReadAll();
	
	List<Tuple> selectReadAllByUserid(String userid);
	
	List<Tuple> search(MainPostSearchDto dto);
	
	List<Tuple> searchMyPost(MyPostSearchDto dto);
	
	Page<Tuple> getPostList(Pageable pageable);
	
	Page<Tuple> getPostListByUserid(String userid, Pageable pageable);
	
	List<Post> getUsersLikesRank();
	
	Page<Post> selectPagedPosts(Pageable pageable);
	
	Page<Post> selectPagedPosts(String userid, Pageable pageable);
	
	List<Post> search(MyPostListSearchDto dto);
	
	List<Post> search(ReviewPostSearchDto dto);
	
	Page<Post> selectPagedP004Posts(Pageable pageable); // origin: reviewpost-mapper -> id='selectPagedPosts'
	
	long selectTotalPostCount(String[] category);
	
	long getTotalCountByUserid(String userid);

	long selectLikes(Long id);
	
	long getTotalCount();
	
	long selectTotalMyPostCount(String userid); // origin: mypost-mapper -> id='selectTotalPostCount'
}