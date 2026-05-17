package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class InquiryDto {
    private Long inquiryId;
    private Long userId;
    private String name;
    private String phone;
    private String email;
    private String category;
    private String title;
    private String content;
    private String status;
    private String answer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
