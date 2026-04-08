package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductImageDto {
    private Long id;
    private Long productId;
    private String imagePath;   // DB 저장 경로 (/uploads/products/...)
    private String imageType;   // THUMBNAIL / DETAIL
    private int sortOrder;
    private LocalDateTime createdAt;
}
