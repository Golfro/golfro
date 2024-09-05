package com.itwill.golfro.repository;

import java.util.List;
import static com.itwill.golfro.domain.QComment.comment;
import static com.itwill.golfro.domain.QUser.user;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.itwill.golfro.domain.Comment;
import com.itwill.golfro.domain.QComment;
import com.itwill.golfro.domain.QPost;
import com.itwill.golfro.domain.QUser;
import com.itwill.golfro.dto.MainCommentItemDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentQuerydslImpl extends QuerydslRepositorySupport implements CommentQuerydsl {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public CommentQuerydslImpl() {
		super(Comment.class);
	}

	@Override
	public Comment selectCommentById(Long id) {
		log.info("selectCommentById(id={})", id);
		
		QComment comment = QComment.comment;
        
		JPQLQuery<Comment> query = from(comment)
				.where(comment.id.eq(id));
		
		Comment result = query.fetchOne();

        return result;
	}

	@Override
	public List<MainCommentItemDto> selectCommentsByPostId(Long postId) {
		log.info("getCommentsByPostId(postId={})", postId);


	    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
        return queryFactory
                .select(Projections.bean(MainCommentItemDto.class,
                    comment.id.as("id"),                   // Comment PK번호
                    user.nickname.as("nickName"),         // User의 닉네임
                    user.image.as("image"),               // User의 프로필 사진
                    comment.content.as("content"),        // 댓글 내용
                    comment.createdTime.as("createdTime"),  // 댓글 최초 작성 시간
                    comment.modifiedTime.as("modifiedTime"), // 댓글 최종 수정 시간
                    comment.selection.as("selection"),    // 선택 여부(0 또는 1)
                    comment.post.id.as("postId")          // Post PK번호
                ))
                .from(comment)
                .leftJoin(comment.user, user)
                .where(comment.post.id.eq(postId))
                .fetch();
        

//	    QComment comment = QComment.comment;
//	    QUser user = QUser.user;
//	    QPost post = QPost.post;
//
//	    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//
//	    List<Comment> result = queryFactory
//	        .select(Projections.constructor(Comment.class,
//	                comment.id,
//	                comment.content,
//	                comment.user.userid,
//	                user.nickname,
//	                user.image))
//	        .from(comment)
//	        .join(user).on(comment.user.userid.eq(user.userid))
//	        .where(comment.post.id.eq(postId))
//	        .orderBy(comment.selection.desc())
//	        .fetch();
//
//	    
//
//	    return result;
	}

	@Override
	public List<Comment> selectCommentsByUserid(String userid) {
		log.info("selectCommentsByUserid(userid={})", userid);

	    QComment comment = QComment.comment;

	    JPQLQuery<Comment> query = from(comment)
	    		.where(comment.user.userid.eq(userid))
	    		.orderBy(comment.modifiedTime.desc());

	    List<Comment> result = query.fetch();
	    
	    return result;
	}

	@Override
	public List<Comment> selectMyCommentsByUserid(String userid) {
		log.info("selectMyCommentsByUserid(userid={})", userid);

	    QComment comment = QComment.comment;
	    QPost post = QPost.post;

	    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

	    List<Comment> result = queryFactory
	        .select(Projections.constructor(Comment.class,
	                comment.id,
	                comment.post.id,
	                post.category.id,
	                comment.content,
	                comment.user.userid,
	                comment.selection,
	                comment.modifiedTime))
	        .from(comment)
	        .join(post).on(comment.post.id.eq(post.id))
	        .where(comment.user.userid.eq(userid))
	        .orderBy(comment.modifiedTime.desc())
	        .fetch();

	    return result;
	}

	@Override
	public List<Comment> selectByPostId(Long postId) {
		log.info("selectByPostId(postId={})", postId);

	    QComment comment = QComment.comment;

	    JPQLQuery<Comment> query = from(comment)
	    		.where(comment.post.id.eq(postId))
	    		.orderBy(comment.id.desc());

	    List<Comment> result = query.fetch();
	    
	    return result;
	}

	@Override
	public long selectCommentCount(Long postId) {
		log.info("selectCommentCount(postId={})", postId);

	    QComment comment = QComment.comment;

	    JPQLQuery<Comment> query = from(comment)
	    		.where(comment.post.id.eq(postId));

	    long result = query.fetchCount();
	    
	    return result;
	}

	@Override
	public List<Comment> selectAllComments(Long postId) {
		QComment comment = QComment.comment;
	    QUser user = QUser.user;

	    JPQLQuery<Comment> query = from(comment)
	        .join(user).on(comment.user.userid.eq(user.userid))
	        .where(comment.post.id.eq(postId))
	        .orderBy(comment.selection.desc());

	    List<Comment> result = query.fetch();
	    
	    return result;
	}
	
}
