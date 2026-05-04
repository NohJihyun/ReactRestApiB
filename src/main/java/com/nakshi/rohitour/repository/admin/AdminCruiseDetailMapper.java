package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CruiseDetailDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminCruiseDetailMapper {
    CruiseDetailDto findByProductId(Long productId);
    void insert(CruiseDetailDto dto);
    void update(CruiseDetailDto dto);
}
