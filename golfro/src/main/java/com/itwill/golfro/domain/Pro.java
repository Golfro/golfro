package com.itwill.golfro.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
@Getter @ToString
@Entity @Table(name = "PROS")
public class Pro {
	
	@Id
	private String id;
	
	private String career;
	
	public Pro updateProCareer(String career) {
        this.career = career;
        
        return this;
    }
	
}
