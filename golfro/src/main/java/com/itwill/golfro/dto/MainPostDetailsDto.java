package com.itwill.golfro.dto;

import java.time.LocalDateTime;

import com.itwill.golfro.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MainPostDetailsDto {
	
	private Long id;
	private String clubName;
	private String name;
	private String title;
	private String content;
	private String nickname;
	private Long views;
	private Long likes;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;
	private Integer selection;
	private String mediaPath;
	
    private Integer height;
    
    private Integer career;
    
    private Integer handy;
    
    private Integer irondistance;
    
    private Integer driverdistance;
    
  
	
	
	public static MainPostDetailsDto fromEntity(Post entity) {
		return MainPostDetailsDto.builder()
				.id(entity.getId())
				.clubName(entity.getClub().getName())
				.name(entity.getUser().getName())
				.title(entity.getTitle())
				.nickname(entity.getUser().getNickname())
				.content(entity.getContent())
				.views(entity.getViews())
				.likes(entity.getLikes())
				.createdTime(entity.getCreatedTime())
				.modifiedTime(entity.getModifiedTime())
				.selection(entity.getSelection())
				.mediaPath(entity.getMedia())
				.height(entity.getHeight())
				.career(entity.getCareer())
				.handy(entity.getHandy())
				.irondistance(entity.getIrondistance())
				.driverdistance(entity.getDriverdistance())
				.build();
	}
	

}
