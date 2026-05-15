package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class BookingDto {
    private Long bookingId;
    private String bookingNumber;
    private Long userId;
    private String userLoginId;
    private Long productId;
    private String productName;
    private String rootCategoryName;
    private String name;
    private String phone;
    private String email;
    private int adultCount;
    private int childCount;
    private int infantCount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate desiredDepartureAt;
    private Long totalPrice;
    private String requestMemo;
    private String status;
    private String adminMemo;
    private boolean adminChecked;
    private String paymentStatus;
    private String paymentMethod;
    private Long paidAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime paidAt;
    private String pgTransactionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
