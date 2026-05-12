package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.AirItineraryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminAirItineraryMapper {

    List<AirItineraryDto> findByProductId(@Param("productId") Long productId);

    AirItineraryDto findById(@Param("id") Long id);

    int insert(AirItineraryDto dto);

    int update(AirItineraryDto dto);

    int delete(@Param("id") Long id);
}
