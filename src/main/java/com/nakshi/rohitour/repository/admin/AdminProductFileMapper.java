package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.ProductFileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductFileMapper {

    List<ProductFileDto> findByProductId(@Param("productId") Long productId);

    ProductFileDto findById(@Param("id") Long id);

    int countByProductId(@Param("productId") Long productId);

    int insert(ProductFileDto dto);

    int delete(@Param("id") Long id);
}
