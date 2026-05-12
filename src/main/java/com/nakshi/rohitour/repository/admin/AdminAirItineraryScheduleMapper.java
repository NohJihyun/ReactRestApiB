package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.AirItineraryScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminAirItineraryScheduleMapper {

    List<AirItineraryScheduleDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    AirItineraryScheduleDto findById(@Param("id") Long id);

    int insert(AirItineraryScheduleDto dto);

    int update(AirItineraryScheduleDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
