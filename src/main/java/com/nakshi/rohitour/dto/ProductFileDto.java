package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductFileDto {
    private Long id;
    private Long productId;
    private String filePath;    // DB 저장 경로 (/uploads/products/...)
    private String fileName;    // 원본 파일명 (다운로드 시 표시)
    private String fileType;    // PDF / EXCEL / WORD / IMAGE / ETC
    private Long fileSize;
    private int sortOrder;
    private LocalDateTime createdAt;
}
