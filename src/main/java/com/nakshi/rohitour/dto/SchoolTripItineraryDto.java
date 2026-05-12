package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SchoolTripItineraryDto {

    private Long id;
    private Long productId;
    private int dayNumber;
    private String title;
    private String description;
    private String hotelName;
    private String shoppingCenterName;
    private String shoppingExchangeInfo;
    private String shoppingInfo;
    private int sortOrder;
    private List<SchoolTripItineraryScheduleDto> schedules;
    private List<SchoolTripItineraryImageDto> images;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
