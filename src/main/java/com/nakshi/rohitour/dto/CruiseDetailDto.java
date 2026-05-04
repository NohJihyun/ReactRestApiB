package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CruiseDetailDto {

    private Long id;
    private Long productId;
    private String includedItems;
    private String excludedItems;
    private String guideName;
    private String guidePhone;
    private String meetingLocation;
    private String meetingTime;
    private String notes;
    private String insuranceInfo;
    private String emergencyContact;
    private String passportVisaInfo;
    private String otherNotices;
    private String ageAdult;
    private String ageChild;
    private String ageInfant;
    private String terms;
    private String reservationNotes;
    private String entryRegulations;
    private String surchargeInfo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
