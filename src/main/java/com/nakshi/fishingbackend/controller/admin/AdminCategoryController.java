package com.nakshi.fishingbackend.controller.admin;

import com.nakshi.fishingbackend.dto.CategoryDto;
import com.nakshi.fishingbackend.service.admin.AdminCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService categoryService;

    public AdminCategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public int insert(@RequestBody CategoryDto dto) {
        return categoryService.insert(dto);
    }

    @PutMapping
    public int update(@RequestBody CategoryDto dto) {
        return categoryService.update(dto);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }

    @GetMapping("/exists")
    public boolean existsByName(@RequestParam String name) {
        return categoryService.existsByName(name);
    }
}
