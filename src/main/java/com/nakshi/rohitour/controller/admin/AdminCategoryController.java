package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.CategoryDto;
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
    public List<CategoryDto> findAll() {

        return categoryService.findAll();
    }
    //카테고리
    //단건조회
   /* @GetMapping("/{id}")
    public CategoryDto findById(@PathVariable Long id) {

        return categoryService.findById(id);
    }*/



    //카테고리 등록
    //@ResponseBody JSON/XML 형태로 바로 응답을 리턴
    @PostMapping
    public int insert(@RequestBody CategoryDto dto) {

        return categoryService.insert(dto);
    }
    //카테고리 수정
    /*@PutMapping
    public int update(@RequestBody CategoryDto dto) {

        return categoryService.update(dto);
    }*/
    //카테고리 삭제
    // @PathVariable
    // URL 경로 변수 값 바인딩
    // 프론트에서 요청시 즉 ,
    // /admin/categories/{id} 로 요청 처리할때 사용
  /*  @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {

        return categoryService.delete(id);
    }*/
    //카테고리명중복체크
    //@RequestParam 쿼리 파라미터 값 바인딩 GET방식 url
  /*  @GetMapping("/exists")
    public boolean existsByName(@RequestParam String name) {

        return categoryService.existsByName(name);
    }*/
}
