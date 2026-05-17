package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BookingRequestDto {
    @NotNull
    private Long productId;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    private String email;
    @Min(0)
    private int adultCount;
    @Min(0)
    private int childCount;
    @Min(0)
    private int infantCount;
    private LocalDate desiredDepartureAt;
    private String requestMemo;
}
