package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookingSearchDto {
    private int page = 1;
    private int size = 15;
    private String keyword;
    private String status;
    private String paymentStatus;
    private Long productId;

    public int getOffset() {
        return (page - 1) * size;
    }
}
