package com.itwill.golfro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
