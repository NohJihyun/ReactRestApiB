package com.nakshi.rohitour.repository.client;

import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductFileDto;
import com.nakshi.rohitour.dto.ProductImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientProductMapper {

    /** 대분류 카테고리명 기준으로 게시중·활성 상품 조회 */
    List<ProductDto> findPublishedByRootCategory(@Param("categoryName") String categoryName);

    /** 상품 단건 조회 (게시중·활성만) */
    ProductDto findById(@Param("productId") Long productId);

    /** 상품 이미지 목록 */
    List<ProductImageDto> findImagesByProductId(@Param("productId") Long productId);

    /** 상품 첨부파일 목록 */
    List<ProductFileDto> findFilesByProductId(@Param("productId") Long productId);
}
