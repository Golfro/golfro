package com.itwill.golfro.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.itwill.golfro.domain.Post;
import com.itwill.golfro.domain.QClub;
import com.itwill.golfro.domain.QComment;
import com.itwill.golfro.dto.CommPostSearchDto;
import com.itwill.golfro.dto.JoinPostSearchDto;
import com.itwill.golfro.dto.MainPostSearchDto;
import com.itwill.golfro.dto.MyPostListSearchDto;
import com.itwill.golfro.dto.MyPostSearchDto;
import com.itwill.golfro.dto.ReviewPostSearchDto;
import com.itwill.golfro.domain.QPost;
import com.itwill.golfro.domain.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostQuerydslImpl extends QuerydslRepositorySupport implements PostQuerydsl {

	@PersistenceContext
	private EntityManager entityManager;

	public PostQuerydslImpl() {
		super(Post.class);
	}

	@Override
	public Post findPreviousPost(String[] category, LocalDateTime teeoff) {
		log.info("findPreviousPost(category={}, createdTime={})", category, teeoff);

		QPost post = QPost.post;

		LocalDateTime now = LocalDateTime.now();

		JPQLQuery<Post> query = from(post)
				.where(post.teeoff.gt(now).and(post.teeoff.lt(teeoff)).and(post.category.id.in(category)))
				.orderBy(post.teeoff.desc()).limit(1);

		Post result = query.fetchOne();

		return result;
	}

	@Override
	public Post findNextPost(String[] category, LocalDateTime teeoff) {
		log.info("findNextPost(category={}, createdTime={})", category, teeoff);

		QPost post = QPost.post;

		LocalDateTime now = LocalDateTime.now();

		JPQLQuery<Post> query = from(post)
				.where(post.teeoff.gt(now).and(post.teeoff.gt(teeoff)).and(post.category.id.in(category)))
				.orderBy(post.teeoff.asc()).limit(1);

		Post result = query.fetchOne();

		return result;
	}

	// 커뮤니티 게시판 이전글 찾기
	@Override
	public Post findPreviousPostComm(String[] category, LocalDateTime createdTime) {
		log.info("findPreviousPost(category={}, createdTime={})", category, createdTime);

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.createdTime.lt(createdTime).and(post.category.id.in(category)))
				.orderBy(post.createdTime.desc()).limit(1);

		Post result = query.fetchOne();

		return result;
	}

	// 커뮤니티 게시판 다음글 찾기
	@Override
	public Post findNextPostComm(String[] category, LocalDateTime createdTime) {
		log.info("findNextPost(category={}, createdTime={})", category, createdTime);

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.createdTime.gt(createdTime).and(post.category.id.in(category)))
				.orderBy(post.createdTime.asc()).limit(1);

		Post result = query.fetchOne();

		return result;
	}

	@Override
	public Post selectById(Long id) {
		log.info("selectById(id={})", id);

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.id.eq(id));

		Post result = query.fetchOne();

		return result;
	}

	@Override
	public Tuple selectByPostId(Long id) {
		log.info("selectByPostId(id={})", id);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		// 서브쿼리: 댓글 존재 여부 확인
		JPQLQuery<?> existsSubquery = JPAExpressions.selectOne().from(comment)
				.where(comment.post.id.eq(post.id).and(comment.selection.eq(1)));

		// 메인 쿼리
		Tuple result = queryFactory
				.select(post.id, club.name, post.title, user.nickname, post.views, post.likes, post.createdTime,
						post.modifiedTime, post.content, post.media, post.height, post.career, post.handy,
						post.ironDistance, post.driverDistance, post.user.userid,
						new CaseBuilder().when(existsSubquery.exists()).then("채택됨").otherwise("미채택"))
				.from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.where(post.id.eq(id)).fetchOne();

		return result;
	}

	@Override
	public Page<Post> selectOrderByIdDesc(String[] categories, Pageable pageable) {
		log.info("selectOrderByIdDesc()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.in(categories)).orderBy(post.createdTime.desc());

		// Paging & Sorting 적용
		getQuerydsl().applyPagination(pageable, query);

		// 한 페이지에 표시할 데이터를 fetch.
		List<Post> list = query.fetch();

		// 전체 레코드 개수를 fetch.
		long count = query.fetchCount();

		// Page<T> 객체를 생성.
		Page<Post> page = new PageImpl<>(list, pageable, count);

		return page;
	}

	@Override
	public List<Post> selectTop5ByF001() {
		log.info("selectTop5ByF001()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.eq("F001")).orderBy(post.likes.desc()).limit(5);

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public List<Post> selectTop5ByF002() {
		log.info("selectTop5ByF002()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.eq("F002")).orderBy(post.likes.desc()).limit(5);

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public List<Post> Fixingthetop() {
		log.info("Fixingthetop()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.user.userid.eq("admin"));

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public List<Post> Fixingthetop2() {
		log.info("Fixingthetop()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.user.userid.eq("admin")).orderBy(post.createdTime.desc()) // 최신
																												// 포스트
																												// 먼저 정렬
				.limit(2); // 상위 2개 결과만 가져오기

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public Page<Post> selectByCategoryAndKeyword(CommPostSearchDto dto, Pageable pageable) {
		log.info("selectByCategoryAndKeyword(dto={}, pageable={})", dto, pageable);

		QPost post = QPost.post;

		// BooleanBuilder를 사용하여 동적 쿼리 작성
		BooleanBuilder whereClause = new BooleanBuilder();

		// 카테고리 조건 추가
		if (dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
			whereClause.and(post.category.id.eq(dto.getCategoryId()));
		}

		// 검색 키워드와 검색 카테고리 조건 추가
		if (dto.getSearchCategory() != null && !dto.getSearchCategory().isEmpty() && dto.getKeyword() != null
				&& !dto.getKeyword().isEmpty()) {
			String searchKeyword = "%" + dto.getKeyword().toUpperCase() + "%"; // 대소문자 구분 없이 검색

			switch (dto.getSearchCategory()) {
			case "t":
				whereClause.and(post.title.likeIgnoreCase(searchKeyword));
				break;
			case "c":
				whereClause.and(post.content.likeIgnoreCase(searchKeyword));
				break;
			case "tc":
				whereClause
						.and(post.title.likeIgnoreCase(searchKeyword).or(post.content.likeIgnoreCase(searchKeyword)));
				break;
			case "n":
				whereClause.and(post.user.userid.likeIgnoreCase(searchKeyword));
				break;
			default:
				break;
			}
		}

		// 카테고리 조건 추가 (F001, F002, F003)
		whereClause.and(post.category.id.in("F001", "F002", "F003"));

		JPQLQuery<Post> query = from(post).where(whereClause).orderBy(post.id.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		// 전체 게시물 수 계산
		long count = query.fetchCount();

		// 페이지 반환
		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public List<Post> selectOrderByTeeoffDesc() {
		log.info("selectOrderByTeeoffDesc()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.eq("F003")).orderBy(post.teeoff.desc());

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public Page<Post> search(JoinPostSearchDto dto, Pageable pageable) {
		log.info("search(dto={})", dto);

		QPost post = QPost.post;

		// BooleanBuilder를 사용하여 동적 쿼리 작성
		BooleanBuilder whereClause = new BooleanBuilder();

		// 검색 키워드 처리
		if (dto.getCategory() != null && !dto.getCategory().isEmpty() && dto.getKeyword() != null
				&& !dto.getKeyword().isEmpty()) {

			String searchKeyword = dto.getKeyword().toUpperCase(); // 대소문자 구분 없이 검색

			switch (dto.getCategory()) {
			case "t":
			case "g":
				searchKeyword = "%" + searchKeyword + "%"; // `%` 추가
				break;
			case "h":
				// `searchKeyword`를 정수로 변환할 때 `%` 제거
				// 예를 들어, `searchKeyword`가 "18"일 경우 정수로 변환
				try {
					Integer.parseInt(searchKeyword); // 입력값이 올바른 정수인지 확인
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				break;
			default:
				searchKeyword = null; // 기본값 설정
				break;
			}

			if (searchKeyword != null) {
				switch (dto.getCategory()) {
				case "t":
					whereClause.and(post.title.likeIgnoreCase(searchKeyword));
					break;
				case "g":
					whereClause.and(post.gcaddress.likeIgnoreCase(searchKeyword));
					break;
				case "h":
					// `searchKeyword`를 정수로 변환하여 비교
					whereClause.and(post.hole.eq(Integer.parseInt(dto.getKeyword())));
					break;
				default:
					break;
				}
			}
		}

		// 'category' 필드가 'P003'인 게시물만 필터링
		whereClause.and(post.category.id.eq("P003"));

		JPQLQuery<Post> query = from(post).where(whereClause).orderBy(post.teeoff.desc());

		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		long count = query.fetchCount();

		Page<Post> page = new PageImpl<>(list, pageable, count);

		return page;
	}

	@Override
	public Page<Post> selectByTeeoffDate(LocalDate teeoffDate, Pageable pageable) {
		log.info("selectByTeeoffDate(teeoffDate={})", teeoffDate);

		QPost post = QPost.post;

		// LocalDate를 LocalDateTime으로 변환
		LocalDateTime startDateTime = teeoffDate.atStartOfDay(); // 시작일의 자정
		LocalDateTime endDateTime = teeoffDate.plusDays(1).atStartOfDay(); // 다음 날의 자정

		JPQLQuery<Post> query = from(post)
				.where(post.teeoff.between(startDateTime, endDateTime).and(post.category.id.eq("P003")))
				.orderBy(post.teeoff.desc());

		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		long count = query.fetchCount();

		Page<Post> page = new PageImpl<>(list, pageable, count);

		return page;
	}

	@Override
	public List<Tuple> selectReadAll() {
		log.info("selectReadAll()");

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		List<Tuple> result = queryFactory
				.select(post.id, club.name, post.title, user.nickname, post.views, post.likes, post.createdTime,
						new CaseBuilder().when(comment.id.isNotNull()).then("해결 완료").otherwise("해결 중"))
				.from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.leftJoin(comment).on(comment.post.id.eq(post.id).and(comment.selection.eq(1)))
				.where(post.category.id.eq("P001")).orderBy(post.id.asc()).fetch();

		return result;
	}

	@Override
	public List<Tuple> selectReadAllByUserid(String userid) {
		log.info("selectReadAllByUserid(userid={})", userid);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		// CASE 문을 위한 서브쿼리
		JPQLQuery<?> existsSubquery = JPAExpressions.selectOne().from(comment)
				.where(comment.post.id.eq(post.id).and(comment.selection.eq(1)));

		// 메인 쿼리
		List<Tuple> result = queryFactory
				.select(post.id, club.name, post.title, user.nickname, post.views, post.likes, post.createdTime,
						new CaseBuilder().when(existsSubquery.exists()).then("해결 완료").otherwise("해결 중"))
				.from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.leftJoin(comment).on(comment.post.id.eq(post.id)).where(user.userid.eq(userid)).orderBy(post.id.asc())
				.fetch();

		return result;
	}

	@Override
	public Page<Post> search(MainPostSearchDto dto, Pageable pageable) {
		log.info("search(searchDto={})", dto);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;

		// 검색어 바인딩
		String searchKeyword = dto.getTextSearchSelect();

		// 메인 쿼리
		JPQLQuery<Post> query = from(post).join(club).on(post.club.id.eq(club.id)).join(user)
				.on(post.user.userid.eq(user.userid)).where(buildSearchConditions(dto, searchKeyword))
				.orderBy(post.id.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		// 결과 가져오기
		List<Post> list = query.fetch();
		log.info("***** list size = {}", list.size());

		// 전체 게시물 수 계산
		long count = query.fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	private BooleanBuilder buildSearchConditions(MainPostSearchDto dto, String searchKeyword) {
		BooleanBuilder builder = new BooleanBuilder();

		QPost post = QPost.post;
		QUser user = QUser.user;

		if (dto.getSearchCategory().equals("searchClubs")) {
			if (dto.getClubSelect().equals("WG"))
				builder.and(post.club.id.eq("WG"));
			if (dto.getClubSelect().equals("PT"))
				builder.and(post.club.id.eq("PT"));
			if (dto.getClubSelect().equals("UT"))
				builder.and(post.club.id.eq("UT"));
			if (dto.getClubSelect().equals("WD"))
				builder.and(post.club.id.eq("WD"));
			if (dto.getClubSelect().equals("DR"))
				builder.and(post.club.id.eq("DR"));
			if (dto.getClubSelect().equals("IR"))
				builder.and(post.club.id.eq("IR"));
		}

		if (dto.getSearchCategory().equals("searchSelection")) {
			if (dto.getSelectSelection().equals("selectFalse"))
				builder.and(post.selection.eq(0));
			if (dto.getSelectSelection().equals("selectTrue"))
				builder.and(post.selection.eq(1));
		}

		if (dto.getSearchCategory().equals("searchTitle")) {
			builder.and(post.title.containsIgnoreCase(searchKeyword));
		}

		if (dto.getSearchCategory().equals("searchContent")) {
			builder.and(post.content.containsIgnoreCase(searchKeyword));
		}

		if (dto.getSearchCategory().equals("searchTitleContent")) {
			builder.and(
					post.title.containsIgnoreCase(searchKeyword).or(post.content.containsIgnoreCase(searchKeyword)));
		}

		if (dto.getSearchCategory().equals("searchAuthor")) {
			builder.and(user.nickname.containsIgnoreCase(searchKeyword));
		}

		return builder;
	}

	@Override
	public Page<Post> searchMyPost(MyPostSearchDto dto, Pageable pageable) {
		log.info("searchMyPost(searchDto={})", dto);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;

		// 검색어 바인딩
		String searchKeyword = "%" + dto.getTextSearchSelect() + "%";

		// 메인 쿼리
		JPQLQuery<Post> query = from(post).join(club).on(post.club.id.eq(club.id)).join(user)
				.on(post.user.userid.eq(user.userid)).where(buildSearchConditions(dto, searchKeyword))
				.orderBy(post.id.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		// 결과 가져오기
		List<Post> list = query.fetch();

		// 전체 게시물 수 계산
		long count = query.fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	private BooleanBuilder buildSearchConditions(MyPostSearchDto dto, String searchKeyword) {
		BooleanBuilder builder = new BooleanBuilder();

		QPost post = QPost.post;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		if ("searchClubs".equals(dto.getSearchCategory())) {
			switch (dto.getClubSelect()) {
			case "WG":
				builder.and(post.club.id.eq("WG"));
				break;
			case "PT":
				builder.and(post.club.id.eq("PT"));
				break;
			case "UT":
				builder.and(post.club.id.eq("UT"));
				break;
			case "WD":
				builder.and(post.club.id.eq("WD"));
				break;
			case "DR":
				builder.and(post.club.id.eq("DR"));
				break;
			case "IR":
				builder.and(post.club.id.eq("IR"));
				break;
			}
		}

		if (dto.getSearchCategory().equals("searchSelection")) {
			if (dto.getSelectSelection().equals("selectTrue")) {
				builder.and(JPAExpressions.selectOne().from(comment)
						.where(comment.post.id.eq(post.id).and(comment.selection.eq(1))).exists());
			}
			if (dto.getSelectSelection().equals("selectFalse")) {
				builder.and(JPAExpressions.selectOne().from(comment)
						.where(comment.post.id.eq(post.id).and(comment.selection.eq(1))).notExists());
			}
		}

		if (dto.getSearchCategory().equals("searchTitle")) {
			builder.and(post.title.containsIgnoreCase(searchKeyword));
		}

		if (dto.getSearchCategory().equals("searchContent")) {
			builder.and(post.content.containsIgnoreCase(searchKeyword));
		}

		if (dto.getSearchCategory().equals("searchTitleContent")) {
			builder.and(
					post.title.containsIgnoreCase(searchKeyword).or(post.content.containsIgnoreCase(searchKeyword)));
		}

		if (dto.getSearchCategory().equals("searchAuthor")) {
			builder.and(user.nickname.containsIgnoreCase(searchKeyword));
		}

		// 사용자의 ID로 필터링
		builder.and(post.user.userid.eq(dto.getUserid()));

		return builder;
	}

	@Override
	public Page<Tuple> getPostList(Pageable pageable) {
		log.info("getPostList(pageable={})", pageable);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		// 서브쿼리: 댓글 존재 여부 확인
		BooleanExpression existsSubquery = JPAExpressions.selectOne().from(comment)
				.where(comment.post.id.eq(post.id).and(comment.selection.eq(1))).exists();

		// 쿼리 생성
		JPAQuery<Tuple> query = queryFactory
				.select(post.id, club.name, post.title, user.nickname, post.views, post.likes, post.createdTime,
						new CaseBuilder().when(existsSubquery).then("해결 완료").otherwise("해결 중"), post.selection)
				.from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.in("P001")).orderBy(post.id.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		// 결과 가져오기
		List<Tuple> list = query.fetch();

		// 전체 게시물 수 계산
		long count = from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.in("P001")).fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<Tuple> getPostListByUserid(String userid, Pageable pageable) {
		log.info("getPostListByUserid(userid={}, pageable={})", userid, pageable);

		QPost post = QPost.post;
		QClub club = QClub.club;
		QUser user = QUser.user;
		QComment comment = QComment.comment;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		// 서브쿼리: 댓글 존재 여부 확인
		BooleanExpression existsSubquery = JPAExpressions.selectOne().from(comment)
				.where(comment.post.id.eq(post.id).and(comment.selection.eq(1))).exists();

		// 쿼리 생성
		JPAQuery<Tuple> query = queryFactory
				.select(post.id, club.name, post.title, user.nickname, post.views, post.likes, post.createdTime,
						new CaseBuilder().when(existsSubquery).then("해결 완료").otherwise("해결 중"), post.selection)
				.from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.where(post.user.userid.eq(userid).and(post.category.id.in("P001")));

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		// 결과 가져오기
		List<Tuple> list = query.fetch();

		// 전체 게시물 수 계산
		long count = from(post).join(club).on(post.club.id.eq(club.id)).join(user).on(post.user.userid.eq(user.userid))
				.where(post.user.userid.eq(userid).and(post.category.id.in("P001"))).fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public List<Post> getUsersLikesRank() {
		log.info("getUsersLikesRank()");

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.eq("P001")).orderBy(post.likes.desc()).limit(10); // 상위
																													// 15개의
																													// 게시물만
																													// 반환

		List<Post> result = query.fetch();

		return result;
	}

	@Override
	public Page<Post> search(MyPostListSearchDto dto, Pageable pageable) {
		log.info("search(dto={}, pageable={})", dto, pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		// 검색어 바인딩
		String searchKeyword = "%" + dto.getKeyword().toUpperCase() + "%";

		// 조건 빌더
		BooleanBuilder builder = new BooleanBuilder();

		// 검색 조건 설정
		if (dto.getCategory() == null) {
			builder.and(
					post.title.containsIgnoreCase(searchKeyword).or(post.content.containsIgnoreCase(searchKeyword)));
		} else {
			switch (dto.getCategory()) {
			case "t":
				builder.and(post.title.containsIgnoreCase(searchKeyword));
				break;
			case "c":
				builder.and(post.content.containsIgnoreCase(searchKeyword));
				break;
			case "tc":
				builder.and(post.title.containsIgnoreCase(searchKeyword)
						.or(post.content.containsIgnoreCase(searchKeyword)));
				break;
			case "n":
				builder.and(user.nickname.containsIgnoreCase(searchKeyword));
				break;
			default:
				break;
			}
		}

		// 공통 조건
		builder.and(post.category.id.ne("P001")).and(post.user.userid.eq(dto.getUserid()));

		JPAQuery<Post> query = queryFactory.selectFrom(post).join(user).on(post.user.userid.eq(user.userid))
				.where(builder).orderBy(post.id.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		// 전체 게시물 수 계산
		List<Post> list = query.fetch();

		// 전체 게시물 수 계산
		long count = from(post).join(user).on(post.user.userid.eq(user.userid)).where(builder).fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<Post> selectPagedPosts(Pageable pageable) {
		log.info("selectPagedPosts(pageable={})", pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPQLQuery<Post> query = from(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.in("P003")).orderBy(post.teeoff.desc());

		List<Post> list = query.fetch();

		long count = query.fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<Post> selectPagedPostsReview(Pageable pageable) {
		log.info("selectPagedPosts(pageable={})", pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPQLQuery<Post> query = from(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.in("P004")).orderBy(post.id.desc());

		// 오프셋과 리밋 적용
		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		List<Post> list = query.fetch();
		long count = query.fetchCount();

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<Post> selectPagedPosts(String userid, Pageable pageable) {
		log.info("selectPagedPosts(userid={}, pageable={})", userid, pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		JPAQuery<Post> query = queryFactory.selectFrom(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.ne("P001").and(post.user.userid.eq(userid))).orderBy(post.createdTime.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		// 전체 게시물 수 계산
		long count = from(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.ne("P001").and(post.user.userid.eq(userid))).fetchCount();

		// 페이지 반환
		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<Post> search(ReviewPostSearchDto dto, Pageable pageable) {
		log.info("search(dto={})", dto);

		QPost post = QPost.post;
		QUser user = QUser.user;

		// 검색어 바인딩
		String searchKeyword = "%" + dto.getKeyword() + "%";

		// 기본 쿼리 생성
		JPQLQuery<Post> query = from(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.eq("P004"));

		// 조건 추가
		if (dto.getCategory() == null) {
			query.where(
					post.title.containsIgnoreCase(searchKeyword).or(post.content.containsIgnoreCase(searchKeyword)));
		} else {
			switch (dto.getCategory()) {
			case "t":
				query.where(post.title.containsIgnoreCase(searchKeyword));
				break;
			case "c":
				query.where(post.content.containsIgnoreCase(searchKeyword));
				break;
			case "tc":
				query.where(post.title.containsIgnoreCase(searchKeyword)
						.or(post.content.containsIgnoreCase(searchKeyword)));
				break;
			case "n":
				query.where(user.nickname.containsIgnoreCase(searchKeyword));
				break;
			default:
				throw new IllegalArgumentException("Invalid category: " + dto.getCategory());
			}
		}

		// 정렬
		query.orderBy(post.id.desc());

		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		long count = query.fetchCount();

		Page<Post> page = new PageImpl<>(list, pageable, count);

		return page;
	}

	@Override
	public Page<Post> selectPagedP004Posts(Pageable pageable) {
		log.info("selectPagedPosts(pageable={})", pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		JPAQuery<Post> query = queryFactory.selectFrom(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.eq("P004")).orderBy(post.createdTime.desc());

		// 페이징 적용
		getQuerydsl().applyPagination(pageable, query);

		List<Post> list = query.fetch();

		// 전체 게시물 수 계산
		long count = from(post).join(user).on(post.user.userid.eq(user.userid)).where(post.category.id.eq("P004"))
				.fetchCount();

		// 페이지 반환
		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public long selectTotalPostCount(String[] category) {
		log.info("selectTotalPostCount(category={})", (Object) category);

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.in(category));

		long result = query.fetchCount();

		return result;
	}

	@Override
	public long getTotalCountByUserid(String userid) {
		log.info("getTotalCountByUserid(userid={})", userid);

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.user.userid.eq(userid).and(post.category.id.eq("P001")));

		long result = query.fetchCount();

		return result;
	}

	@Override
	public long selectLikes(Long id) {
		log.info("selectLikes(id={})", id);

		QPost post = QPost.post;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

		long likes = queryFactory.select(post.likes).from(post).where(post.id.eq(id)).fetchOne();

		return likes;
	}

	@Override
	public long getTotalCount() {
		log.info("getTotalCount()");

		QPost post = QPost.post;

		long count = from(post).where(post.category.id.in("P001")).fetchCount();

		return count;
	}

	@Override
	public long selectTotalMyPostCount(String userid) {
		log.info("selectTotalMyPostCount(userid={})", userid);

		QPost post = QPost.post;

		long count = from(post).where(post.category.id.in("P001").and(post.user.userid.eq(userid))).fetchCount();

		return count;
	}

	@Override
	public Page<Post> selectPagedPostsComm(Pageable pageable) {
		log.info("selectPagedPosts(pageable={})", pageable);

		QPost post = QPost.post;
		QUser user = QUser.user;

		JPQLQuery<Post> query = from(post).join(user).on(post.user.userid.eq(user.userid))
				.where(post.category.id.in("F001", "F002", "F003")).orderBy(post.id.desc());

		// Offset과 Limit을 설정하여 실제 페이징 적용
		List<Post> list = query.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

		long count = query.fetchCount(); // 전체 데이터 수를 가져와 페이지 총 개수를 계산합니다.

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public List<Post> getCommRank() {

		QPost post = QPost.post;

		JPQLQuery<Post> query = from(post).where(post.category.id.in("F001", "F002", "F003")).orderBy(post.likes.desc())
				.limit(10);

		List<Post> result = query.fetch();

		return result;

	}
}
