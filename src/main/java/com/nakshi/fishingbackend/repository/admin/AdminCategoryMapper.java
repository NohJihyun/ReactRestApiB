package com.nakshi.fishingbackend.repository.admin;

import com.nakshi.fishingbackend.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {
    List<CategoryDto> findAll();
    CategoryDto findById(Long id);
    int insert(CategoryDto dto);
    int update(CategoryDto dto);
    int delete(Long id);
    int existsByName(String name);
}