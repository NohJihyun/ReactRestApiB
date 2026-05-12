package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.SchoolTripItineraryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminSchoolTripItineraryMapper {

    List<SchoolTripItineraryDto> findByProductId(@Param("productId") Long productId);

    SchoolTripItineraryDto findById(@Param("id") Long id);

    int insert(SchoolTripItineraryDto dto);

    int update(SchoolTripItineraryDto dto);

    int delete(@Param("id") Long id);
}
