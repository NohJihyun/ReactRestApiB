package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.DomesticItineraryImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDomesticItineraryImageMapper {

    List<DomesticItineraryImageDto> findByItineraryId(@Param("itineraryId") Long itineraryId);

    DomesticItineraryImageDto findById(@Param("id") Long id);

    int insert(DomesticItineraryImageDto dto);

    int delete(@Param("id") Long id);

    int deleteByItineraryId(@Param("itineraryId") Long itineraryId);
}
