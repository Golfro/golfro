package com.itwill.golfro.domain;

import java.io.Serializable;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
// onlyExplicitlyIncluded 속성: @EqualsAndHashCode.Include 애너테이션이 설정된 필드만 사용할 것인지 여부.
// callSuper 속성: superclass의 equals(), hashCode() 메서드를 사용할 것인지 여부.
@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

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
	@Column(unique = true)
	private String nickname;
	
	@Basic(optional = false)
	private String name;
	
	private String image;
	
	@Basic(optional = false)
	private Long birth;
	
	private Integer gender;
	
	@Basic(optional = false)
	@Column(unique = true)
	private String phone;
	
	@Basic(optional = false)
	private String email;
	
	private String address;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRO_ID")
	private Pro pro;
	
	private String account;
	
	private Long point;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	private Grade grade;
	
	private String accept;
	
	private Long withdraw;
	
	@PrePersist
    private void prePersist() {
        if (this.grade == null) {
            this.grade = Grade.builder().id("G21").name("일반 사용자(흰공)").build();
        }
        
        if (this.point == null) {
        	this.point = 0L;
        }
        
        if (this.image == null) {
        	this.image = "https://golfro-bucket.s3.ap-northeast-2.amazonaws.com/basic_profile.png";
        }
    }
	
	public User update(String password, String phone, String address, String account) {
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.account = account;
        
        return this;
    }
	
	public User updateImage(String image) {
        this.image = image;
        
        return this;
    }
	
	public User updateNickname(String nickname) {
        this.nickname = nickname;
        
        return this;
    }
	
	public User updatePoint(Long withdraw) {
		this.point -= withdraw;
		
		return this;
	}
	
	public User updateGrade(String grade) {
		this.grade = Grade.builder().id(grade).build();
		
		return this;
	}
	
	public User increasePoint(Long commentsId) {
        this.point = point + 300;
        
        return this;
    }
	
	public User upgrade(String proId) {
        this.accept = null;
        this.grade = Grade.builder().id("G10").build();
        this.pro = Pro.builder().id(proId).build();
        
        return this;
    }
	
	public User reject(String userid) {
		this.accept = null;
		
		return this;
	}
	
}
