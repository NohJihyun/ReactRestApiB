package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.AirDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminAirDetailMapper {

    AirDetailDto findByProductId(@Param("productId") Long productId);

    int insert(AirDetailDto dto);

    int update(AirDetailDto dto);
}
