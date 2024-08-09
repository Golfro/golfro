package com.itwill.golfro.domain;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
// onlyExplicitlyIncluded 속성: @EqualsAndHashCode.Include 애너테이션이 설정된 필드만 사용할 것인지 여부.
// callSuper 속성: superclass의 equals(), hashCode() 메서드를 사용할 것인지 여부.
@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@EqualsAndHashCode.Include // userid 필드를 equals()와 hashCode()를 재정의할 때 사용.
	@NaturalId // UNIQUE 제약조건
	@Basic(optional = false) // NOT NULL 제약조건
	@Column(updatable = false) // UPDATE 쿼리에서 SET 절에서 제외하기 위해서.
	private String userid;
	
	@Basic(optional = false)
	private String password;
	
	@Basic(optional = false)
	@NaturalId
	private String nickname;
	
	@Basic(optional = false)
	private String name;
	
	private String image;
	
	@Basic(optional = false)
	private Long birth;
	
	private Integer gender;
	
	@Basic(optional = false)
	@NaturalId
	private String phone;
	
	@Basic(optional = false)
	private String email;
	
	private String address;
	
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRO_ID")
	private Pro pro;
	
	private String account;
	
	private Long point;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	private Grade grade;
	
	private String accept;
	
	private Long withdraw;
	
	public User update(String password, String nickname, String image, String phone, String address, String account) {
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.phone = phone;
        this.address = address;
        this.account = account;
        
        return this;
    }
	
}
