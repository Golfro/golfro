package com.itwill.golfro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Grade;

public interface GradeRepository extends JpaRepository<Grade, String> {

}
