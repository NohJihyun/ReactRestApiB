package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AirDetailDto {

    private Long id;
    private Long productId;

    // 가격
    private Long priceAdult;
    private Long priceChild;
    private Long priceInfant;
    private String ageAdult;
    private String ageChild;
    private String ageInfant;

    // 항공편 정보
    private String flightInfo;

    // 포함 / 불포함
    private String includedItems;
    private String excludedItems;

    // 가이드 미팅정보
    private String guideName;
    private String guidePhone;
    private String meetingLocation;
    private String meetingTime;
    private String notes;

    // 유의사항
    private String insuranceInfo;
    private String emergencyContact;
    private String passportVisaInfo;
    private String otherNotices;

    // 약관 / 규정
    private String surchargeInfo;
    private String terms;
    private String reservationNotes;
    private String entryRegulations;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
