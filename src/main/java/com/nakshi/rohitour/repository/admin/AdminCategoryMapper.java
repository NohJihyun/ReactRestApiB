package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CategoryDto;
import com.nakshi.rohitour.dto.CategorySearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {
    // 목록 + 검색
    // List<CategoryDto> findAll(CategorySearchDto searchDto);

    // 목록 + 검색 + 페이지네이션
    List<CategoryDto> findAll(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("search") CategorySearchDto searchDto
    );
    // 페이지네이션 카운트
    int countAll(@Param("search") CategorySearchDto searchDto);

    int insert(CategoryDto dto);
    int update(CategoryDto dto);
    int deactivate(Long id);
    int delete(Long id);
    int countDuplicate(
            int depth,
            Long parentId,
            int sortOrder,
            Long excludeId
    );
}