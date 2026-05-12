package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.DomesticDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminDomesticDetailMapper {

    DomesticDetailDto findByProductId(@Param("productId") Long productId);

    int insert(DomesticDetailDto dto);

    int update(DomesticDetailDto dto);
}
