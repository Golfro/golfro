package com.itwill.golfro.dto;

import java.util.List;

import lombok.Data;


@Data
public class MainPostPageDto {
    private List<MainPostListDto> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }
}