package com.itwill.golfro.domain;

import jakarta.persistence.Basic;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
@Getter @ToString @EqualsAndHashCode(callSuper = true)
@Entity @Table(name = "COMMENTS")
public class Comment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Basic(optional = false)
	@ToString.Exclude // toString 메서드를 만들 때 제외시킴.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_ID") // FK 제약조건이 있는 컬럼 이름.
	private Post post;
	
	@Basic(optional = false)
	private String content;
	
	@Basic(optional = false)
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")
	private User user;
	
	@Builder.Default
	private Integer selection = 0;
	
	public Comment update(String content) {
		this.content = content;
		return this;
	}
	
	
	public Comment selectionConfirm() {
        this.selection = 1;
        
        return this;
    }
}
