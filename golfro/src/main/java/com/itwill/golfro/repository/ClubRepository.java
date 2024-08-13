package com.itwill.golfro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwill.golfro.domain.Club;

public interface ClubRepository extends JpaRepository<Club, String> {

}
