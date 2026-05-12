package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.DomesticItineraryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDomesticItineraryMapper {

    List<DomesticItineraryDto> findByProductId(@Param("productId") Long productId);

    DomesticItineraryDto findById(@Param("id") Long id);

    int insert(DomesticItineraryDto dto);

    int update(DomesticItineraryDto dto);

    int delete(@Param("id") Long id);
}
