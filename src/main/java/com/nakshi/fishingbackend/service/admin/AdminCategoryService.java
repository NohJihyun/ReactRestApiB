package com.nakshi.fishingbackend.service.admin;

import com.nakshi.fishingbackend.dto.CategoryDto;
import com.nakshi.fishingbackend.repository.admin.AdminCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCategoryService {

    private final AdminCategoryMapper categoryMapper;

    public AdminCategoryService(AdminCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDto> findAll() {
        return categoryMapper.findAll();
    }

    public CategoryDto findById(Long id) {
        return categoryMapper.findById(id);
    }

    public int insert(CategoryDto dto) {
        return categoryMapper.insert(dto);
    }

    public int update(CategoryDto dto) {
        return categoryMapper.update(dto);
    }

    public int delete(Long id) {
        return categoryMapper.delete(id);
    }

    public boolean existsByName(String name) {
        return categoryMapper.existsByName(name) > 0;
    }
}
