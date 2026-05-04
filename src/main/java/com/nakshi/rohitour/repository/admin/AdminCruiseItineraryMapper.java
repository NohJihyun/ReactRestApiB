package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CruiseItineraryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminCruiseItineraryMapper {

    List<CruiseItineraryDto> findByProductId(@Param("productId") Long productId);

    CruiseItineraryDto findById(@Param("id") Long id);

    int insert(CruiseItineraryDto dto);

    int update(CruiseItineraryDto dto);

    int delete(@Param("id") Long id);
}
