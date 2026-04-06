package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchDto {
    private Long categoryId;
    private String status;
    private String isFeatured;
    private String isActive;
    private String keyword;
}
