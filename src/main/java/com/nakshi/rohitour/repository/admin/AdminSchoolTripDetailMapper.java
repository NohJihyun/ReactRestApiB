package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.SchoolTripDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminSchoolTripDetailMapper {

    SchoolTripDetailDto findByProductId(@Param("productId") Long productId);

    int insert(SchoolTripDetailDto dto);

    int update(SchoolTripDetailDto dto);
}
