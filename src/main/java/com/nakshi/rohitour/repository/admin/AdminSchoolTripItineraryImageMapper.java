package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.SchoolTripItineraryImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminSchoolTripItineraryImageMapper {

    List<SchoolTripItineraryImageDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    List<SchoolTripItineraryImageDto> findByScheduleId(@Param("scheduleId") Long scheduleId);

    SchoolTripItineraryImageDto findById(@Param("id") Long id);

    int insert(SchoolTripItineraryImageDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
