package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.SchoolTripItineraryScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminSchoolTripItineraryScheduleMapper {

    List<SchoolTripItineraryScheduleDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    SchoolTripItineraryScheduleDto findById(@Param("id") Long id);

    int insert(SchoolTripItineraryScheduleDto dto);

    int update(SchoolTripItineraryScheduleDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
