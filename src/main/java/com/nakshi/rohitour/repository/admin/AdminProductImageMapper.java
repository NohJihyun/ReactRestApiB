package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.ProductImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductImageMapper {

    List<ProductImageDto> findByProductId(@Param("productId") Long productId);

    ProductImageDto findThumbnail(@Param("productId") Long productId);

    int countDetail(@Param("productId") Long productId);

    int insert(ProductImageDto dto);

    int delete(@Param("id") Long id);

    int updateOrder(@Param("id") Long id, @Param("sortOrder") int sortOrder);
}
