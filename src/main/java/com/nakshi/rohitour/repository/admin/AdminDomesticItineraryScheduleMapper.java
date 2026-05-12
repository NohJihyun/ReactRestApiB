package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.DomesticItineraryScheduleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDomesticItineraryScheduleMapper {

    List<DomesticItineraryScheduleDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    DomesticItineraryScheduleDto findById(@Param("id") Long id);

    int insert(DomesticItineraryScheduleDto dto);

    int update(DomesticItineraryScheduleDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
