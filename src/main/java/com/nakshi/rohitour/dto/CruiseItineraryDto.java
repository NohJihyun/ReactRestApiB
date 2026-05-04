package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CruiseItineraryDto {

    private Long id;
    private Long productId;
    private int dayNumber;
    private String title;
    private String description;
    private String shoppingCenterName;
    private String shoppingExchangeInfo;
    private String shoppingInfo;
    private String hotelName;
    private int sortOrder;
    private java.util.List<CruiseItineraryScheduleDto> schedules;
    private java.util.List<CruiseItineraryImageDto> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
