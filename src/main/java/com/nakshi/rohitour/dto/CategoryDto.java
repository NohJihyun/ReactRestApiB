package com.nakshi.rohitour.dto;

import java.time.LocalDateTime;

/* 목록 DTO */
public class CategoryDto {

    private Long categoryId;                 // PK
    private Long parentId;                   // 상위 카테고리

    private String categoryCode;            // 시스템코드
    private String categoryName;            // 화면 노출명

    private Integer depth;                  // 1:대분류, 2:소분류
    private Integer sortOrder;              // 정렬

    private String isActive;                // 사용여부 Y/N

    private LocalDateTime createdAt;        // 생성일
    private LocalDateTime updatedAt;        // 수정일

    // ===== Getter / Setter =====

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
