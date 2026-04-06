package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private Long categoryId;
    private String categoryName;        // JOIN 결과, 목록 표시용

    private String productCode;
    private String productName;
    private String productSubname;
    private String summary;
    private String description;
    private String thumbnailUrl;
    private String status;              // DRAFT / PUBLISHED / HIDDEN / ENDED
    private String isFeatured;          // Y / N
    private String isActive;            // Y / N

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime exposureStartAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime exposureEndAt;

    private String seoTitle;
    private String seoDescription;
    private Integer viewCount;
    private Long createdBy;
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
