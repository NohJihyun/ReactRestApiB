package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CruiseItineraryScheduleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCruiseItineraryScheduleMapper {
    List<CruiseItineraryScheduleDto> findByItineraryId(Long itineraryId);
    CruiseItineraryScheduleDto findById(Long id);
    void insert(CruiseItineraryScheduleDto dto);
    void update(CruiseItineraryScheduleDto dto);
    void delete(Long id);
    void deleteByItineraryId(Long itineraryId);
}
