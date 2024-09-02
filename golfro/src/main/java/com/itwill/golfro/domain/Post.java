package com.itwill.golfro.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
@Getter
@ToString(callSuper = true) // 상위 클래스의 toString()을 호출해서 toString() 메서드를 작성.
@EqualsAndHashCode(callSuper = true) // 상위 클래스의 필드들도 사용해서 equals(), hashCode() 작성.
@Entity @Table(name = "POSTS")
public class Post extends BaseTimeEntity {
    
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generated as identity
    private Long id;
    
    @Basic(optional = false) // not null
    private String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLUB_ID")
    private Club club;
    
    private String media;
    
    @Basic(optional = false)
    private String content;
    
    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;
    
    @Basic(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID")
    private Category category;
    
    private Integer height;
    
    private Integer career;
    
    private Integer handy;
    
    private Integer ironDistance;
    
    private Integer driverDistance;
    
    private Integer hole;
    
    private String gcaddress;
    
    private Long greenfee;
    
    private LocalDateTime teeoff;
    
    private Long likes;
    
    private Long views;
    
    private Integer selection;
    
    @PostLoad
    private void setDefaultValues() {
        if (this.likes == null) {
            this.likes = 0L;
        }
        if (this.views == null) {
            this.views = 0L;
        }
        if (this.selection == null) {
            this.selection = 0;
        }
    }
    
    // update 기능에서 사용할 공개 메서드
    public Post update(String title, String content, Category category, String media) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.media = media;
        
        return this;
    }
    
    public Post increaseViews(Long views) {
    	this.views = views + 1;
    	
    	return this;
    }
    
    public Post increaseLikes(Long likes) {
    	this.likes = likes + 1;
    	
    	return this;
    }
    
}
