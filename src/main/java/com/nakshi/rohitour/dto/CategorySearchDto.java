package com.nakshi.rohitour.dto;

/* 검색 DTO */
public class CategorySearchDto {

    private Integer depth;     // 대분류 / 소분류
    private Long parentId;     // 상위 카테고리
    private String isActive;   // 사용여부 (Y/N)
    private String keyword;    // 검색어 (이름 / 코드)
    private Long categoryId;   // 특정 카테고리 자체 선택 추가
    
    // getter / setter

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}

