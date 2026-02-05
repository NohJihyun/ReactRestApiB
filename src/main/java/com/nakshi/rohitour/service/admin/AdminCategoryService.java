package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.CategoryDto;
import com.nakshi.rohitour.dto.CategorySearchDto;
import com.nakshi.rohitour.repository.admin.AdminCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCategoryService {

    private final AdminCategoryMapper categoryMapper;

    public AdminCategoryService(AdminCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    /* 목록리스트 + 검색조건 DTO */
    public List<CategoryDto> findAll(CategorySearchDto searchDto) {
        return categoryMapper.findAll(searchDto);
    }

    /* 등록 */
    public int insert(CategoryDto dto) {

        return categoryMapper.insert(dto);
    }
    /* 수정 */
    public int update(CategoryDto dto) {
        return categoryMapper.update(dto);
    }
   /* 논리 => 비활성화 */
    public int deactivate(Long id) {
        return categoryMapper.deactivate(id);
    }

    /* 물리 => 삭제 */
    public int delete(Long id) {
        return categoryMapper.delete(id);
    }
}
