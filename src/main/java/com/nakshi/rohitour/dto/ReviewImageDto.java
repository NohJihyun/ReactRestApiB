package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewImageDto {
    private Long id;
    private Long reviewId;
    private String imagePath;
    private int sortOrder;
}
