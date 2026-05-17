package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InquiryRequestDto {
    @NotBlank private String name;
    @NotBlank private String phone;
    @NotBlank @Email private String email;
    @NotBlank private String category;
    @NotBlank private String title;
    @NotBlank private String content;
}
