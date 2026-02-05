package com.nakshi.rohitour.repository.admin;

import com.nakshi.rohitour.dto.CategoryDto;
import com.nakshi.rohitour.dto.CategorySearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {
    List<CategoryDto> findAll(CategorySearchDto searchDto);
    int insert(CategoryDto dto);
    int update(CategoryDto dto);
    int deactivate(Long id);
    int delete(Long id);
}