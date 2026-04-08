package com.nakshi.rohitour.repository.client;

import com.nakshi.rohitour.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientProductMapper {

    /** 대분류 카테고리명 기준으로 게시중·활성 상품 조회 */
    List<ProductDto> findPublishedByRootCategory(@Param("categoryName") String categoryName);
}
