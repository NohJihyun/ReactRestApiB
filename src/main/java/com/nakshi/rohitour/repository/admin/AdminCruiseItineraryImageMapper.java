package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CruiseItineraryImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminCruiseItineraryImageMapper {

    List<CruiseItineraryImageDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    List<CruiseItineraryImageDto> findByScheduleId(@Param("scheduleId") Long scheduleId);

    CruiseItineraryImageDto findById(@Param("id") Long id);

    int insert(CruiseItineraryImageDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
