package com.itwill.golfro.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.itwill.golfro.domain.QPro;
import com.itwill.golfro.domain.QUser;
import com.itwill.golfro.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserQuerydslImpl extends QuerydslRepositorySupport implements UserQuerydsl {

	@Autowired
    private JPAQueryFactory queryFactory;
	
	public UserQuerydslImpl() {
		super(User.class);
	}

	@Override
	public User selectProByUserid(String userid) {
		log.info("selectProByUserid(userid={})", userid);
		
		QUser user = QUser.user;
        QPro pro = QPro.pro;

        User result = queryFactory.selectFrom(user)
                .leftJoin(pro).on(user.pro.id.eq(pro.id))
                .where(user.userid.eq(userid))
                .fetchOne();

        return result;
	}

	@Override
	public User selectByUserid(String userid) {
		log.info("selectByUserid(userid={})", userid);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.userid.eq(userid));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User selectByNickname(String nickname) {
		log.info("selectByNickname(nickname={})", nickname);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.nickname.eq(nickname));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User selectByEmail(String email) {
		log.info("selectByEmail(email={})", email);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.email.eq(email));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User selectByPhone(String phone) {
		log.info("selectByPhone(phone={})", phone);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.phone.eq(phone));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User selectByAccept(String accept) {
		log.info("selectByAccept(accept={})", accept);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.pro.id.eq(accept)
						.and(user.accept.eq(accept)));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User selectByUseridAndPassword(String userid, String password) {
		log.info("selectByUseridAndPassword(userid={}, password={})", userid, password);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.userid.eq(userid)
						.and(user.password.eq(password)));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User FindUserid(String name, String email) {
		log.info("selectByUseridAndPassword(name={}, email={})", name, email);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.name.eq(name)
						.and(user.email.eq(email)));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public User FindPassword(String userid, String email, String phone) {
		log.info("FindPassword(userid={}, email={}, phone={})", userid, email, phone);
		
		QUser user = QUser.user;
        
		JPQLQuery<User> query = from(user)
				.where(user.userid.eq(userid)
						.and(user.email.eq(email))
						.and(user.phone.eq(phone)));
		
		User result = query.fetchOne();

        return result;
	}

	@Override
	public List<User> getUsersPointRank() {
		log.info("getUsersPointRank()");
		
		QUser user = QUser.user;

		JPQLQuery<User> query = from(user)
				.orderBy(user.point.desc())
                .limit(5);
		
		List<User> result = query.fetch();

        return result;
	}

	@Override
	public List<User> AdminSignup() {
		log.info("AdminSignup()");
		
		QUser user = QUser.user;

		JPQLQuery<User> query = from(user)
				.where(user.accept.isNotNull());
		
		List<User> result = query.fetch();

        return result;
	}

	@Override
	public List<User> AdminExchange() {
		log.info("AdminExchange()");
		
		QUser user = QUser.user;

		JPQLQuery<User> query = from(user)
				.where(user.withdraw.isNotNull()
	            .and(user.withdraw.ne(0L)));
		
		List<User> result = query.fetch();

        return result;
	}

	@Override
	public List<User> AllMembers() {
		log.info("AllMembers()");
		
		QUser user = QUser.user;

		JPQLQuery<User> query = from(user)
				.orderBy(user.userid.asc());
		
		List<User> result = query.fetch();

        return result;
	}

	@Override
	public Map<String, String> findAllUserNicknames() {
		log.info("findAllUserNicknames()");
		
		QUser user = QUser.user;

		List<Tuple> results = queryFactory.select(user.userid, user.nickname)
                .from(user)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(user.userid),
                        tuple -> tuple.get(user.nickname)
                ));
	}

	@Override
	public String FindNicknameByUserId(String userid) {
		log.info("FindNicknameByUserId(userid={})", userid);
		
		QUser user = QUser.user;

        String nickname = queryFactory.select(user.nickname)
                .from(user)
                .where(user.userid.eq(userid))
                .fetchOne();

        return nickname;
	}
	
}
