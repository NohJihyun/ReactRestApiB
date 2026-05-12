package com.nakshi.rohitour.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolTripItineraryScheduleDto {

    private Long id;
    private Long itineraryId;
    private String time;
    private String description;
    private int sortOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
