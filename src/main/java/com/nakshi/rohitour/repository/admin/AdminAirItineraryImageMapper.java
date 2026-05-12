package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.AirItineraryImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminAirItineraryImageMapper {

    List<AirItineraryImageDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    AirItineraryImageDto findById(@Param("id") Long id);

    int insert(AirItineraryImageDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
