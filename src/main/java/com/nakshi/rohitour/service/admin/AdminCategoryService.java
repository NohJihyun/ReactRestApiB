package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.dto.CategoryDto;
import com.nakshi.rohitour.dto.CategorySearchDto;
import com.nakshi.rohitour.exception.DuplicateException;
import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;

import com.nakshi.rohitour.repository.admin.AdminCategoryMapper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdminCategoryService {

    private final AdminCategoryMapper categoryMapper;

    public AdminCategoryService(AdminCategoryMapper categoryMapper) {

        this.categoryMapper = categoryMapper;
    }
    /* 목록리스트 + 검색조건 DTO
    public List<CategoryDto> findAll(CategorySearchDto searchDto) {
        return categoryMapper.findAll(searchDto);
    }
    */

    /* 목록 + 검색 + 페이지네이션 */
    public PageResponse<CategoryDto> findAll(
            PageRequest pageRequest,
            CategorySearchDto searchDto
    ) {
        // 1️ 페이징된 목록 조회
        List<CategoryDto> list =
                categoryMapper.findAll(
                        pageRequest.getOffset(),
                        pageRequest.getSize(),
                        searchDto
                );

        // 2️ 전체 건수 조회 (검색 조건 동일)
        int totalCount =
                categoryMapper.countAll(searchDto);

        // 3️ PageResponse로 감싸서 반환
        return new PageResponse<>(list, totalCount, pageRequest);
    }

    /* 등록 */
    public int insert(CategoryDto dto) {
        validateDuplicate(dto, null);   // 등록 값 검증 추가
        return categoryMapper.insert(dto);
    }
    /* 수정 */
    public int update(CategoryDto dto) {
        validateDuplicate(dto, dto.getCategoryId()); //자기 자신 제외
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

    //  컨트롤러에서 호출할 공개용 메서드
    public void checkDuplicate(String categoryCode, int sortOrder, Long excludeId) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryCode(categoryCode);
        dto.setSortOrder(sortOrder);

        validateDuplicate(dto, excludeId);
    }

    /* ===== 내부 전용 검증 ===== */
    public void validateDuplicate(CategoryDto dto, Long excludeId) {
        int count = categoryMapper.countDuplicate(
                dto.getDepth(),
                dto.getParentId(),
                dto.getSortOrder(),
                excludeId
        );

        if (count > 0) {
            throw new DuplicateException("이미 사용 중인 정렬 순서입니다.");
        }
    }
}

