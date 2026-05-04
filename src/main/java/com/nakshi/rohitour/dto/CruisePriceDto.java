package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CruisePriceDto {

    private Long id;
    private Long productId;
    private String cabinType;
    private Long priceAdult;
    private Long priceChild;
    private Long priceInfant;
    private int sortOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
