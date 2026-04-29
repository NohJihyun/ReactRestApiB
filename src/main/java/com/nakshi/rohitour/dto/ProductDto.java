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

    private String videoPath;
    private String videoUrl;
    private String travelType;             // INDIVIDUAL / GROUP / BOTH
    private Integer minPeople;
    private Integer maxPeople;
    private java.math.BigDecimal pricePerPerson;

    // 목록 표시용 미디어 집계 (JOIN/서브쿼리)
    private String thumbnailPath;   // product_images에서 JOIN
    private int    imageCount;      // 전체 이미지 수 (썸네일 포함)
    private int    fileCount;       // 첨부파일 수

    private String transportType;
    private String hasShopping;
    private String hasGuideFee;
    private String hasEscort;
    private String hasOptionalTour;

    private String departureLocation;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureAt;

    private String arrivalLocation;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalAt;

    private String hashtags;

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
