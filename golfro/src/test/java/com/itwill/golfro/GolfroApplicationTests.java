package com.itwill.golfro;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.golfro.domain.Category;
import com.itwill.golfro.domain.User;
import com.itwill.golfro.repository.CategoryRepository;
import com.itwill.golfro.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class GolfroApplicationTests {
	
	@Autowired
	private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepository;

 // @Test
    public void testCreatePostWithCategory() {
        // 데이터베이스에서 카테고리 조회
        Category category = categoryRepository.findById("p001").get();
        assertThat(category).isNotNull();
    }

    @Test
    public void testUserid() {
    	User user = userRepo.findByUserid("ksm3925");
    	assertThat(user).isNotNull();
    }

}
