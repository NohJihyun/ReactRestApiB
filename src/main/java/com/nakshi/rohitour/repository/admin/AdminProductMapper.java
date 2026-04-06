package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminProductMapper {

    List<ProductDto> findAll(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("search") ProductSearchDto search
    );

    int countAll(@Param("search") ProductSearchDto search);

    int countByCode(
            @Param("productCode") String productCode,
            @Param("excludeId") Long excludeId
    );

    int insert(ProductDto dto);

    int update(ProductDto dto);

    int deactivate(Long id);

    int delete(Long id);
}
