package com.nakshi.rohitour.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BookingRequestDto {
    private Long productId;
    private String name;
    private String phone;
    private String email;
    private int adultCount;
    private int childCount;
    private int infantCount;
    private LocalDate desiredDepartureAt;
    private String requestMemo;
}
