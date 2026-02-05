package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.CategoryDto;
import com.nakshi.rohitour.dto.CategorySearchDto;
import com.nakshi.rohitour.service.admin.AdminCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//RESTfUL " URL=자원(Resource),HTTP Method=행위(Action) "
// get = 조회 , post 생성 , put 전체수정, patch 부분수정
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService categoryService;

    public AdminCategoryController(AdminCategoryService categoryService) {

        this.categoryService = categoryService;
    }
    //list 목록조회 URL: /admin/categories
    //리턴타입 list , 배열
    //리턴시 view 네임, 문자열 아닐시 스프링이 감지해
    //@RestController 로 인해 view가 아닌 응답 바디로 처리
    //응답바디로 사용 JACKSON 을 활용해 JSON으로 내려줌.
    @GetMapping
    public List<CategoryDto> findAll(CategorySearchDto searchDto) {

        return categoryService.findAll(searchDto);
    }

    //카테고리 등록
    //@RequestBody JSON/XML "" 문자열 형태 > 자바객체변환 응답을 리턴
    //CategoryDto 타입/클래스 로 변환후 , dto 변수에 담아서 전달
    @PostMapping
    public int insert(@RequestBody CategoryDto dto) {

        return categoryService.insert(dto);
    }

    //카테고리 수정
    //전체수정
    //@RequestBody JSON/XML "" 문자열 형태 > 자바객체변환 응답을 리턴
    //CategoryDto 타입/클래스 로 변환후 , dto 변수에 담아서 전달
    //프론트 요청 /admin/categories/${id}
    //@PathVariable 요청 URL 경로에 포함된 id 값을 꺼내서 메서드 파라미터로 사용한다
    @PutMapping("/{id}")
    public int update(
            @PathVariable Long id,
            @RequestBody CategoryDto dto
    ) {
        dto.setCategoryId(id); // id를 DTO에 세팅
        return categoryService.update(dto);
    }
    //비활성화
    // 논리삭제 => 일반운영=> 비활성화 처리
    @DeleteMapping("/{id}")
    public int deactivate(@PathVariable Long id) {
        return categoryService.deactivate(id);
    }
    //물리삭제
    //관리자, 테스트, 잘못 만든 데이터
    //@PathVariable : url / 이어진 경로에 들어온 파라미터 처리
    //@RequestParam : url 에 쿼리스트링으로 들어온 파라미터 처리
    @DeleteMapping("/{id}/delete")
    public int delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }

}
