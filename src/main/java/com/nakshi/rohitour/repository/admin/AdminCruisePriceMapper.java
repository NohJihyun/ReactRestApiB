package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CruisePriceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCruisePriceMapper {
    List<CruisePriceDto> findByProductId(Long productId);
    CruisePriceDto findById(Long id);
    void insert(CruisePriceDto dto);
    void update(CruisePriceDto dto);
    void delete(Long id);
    void deleteByProductId(Long productId);
}
