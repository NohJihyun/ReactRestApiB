package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.service.client.ClientProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ClientProductController {

    private final ClientProductService service;

    public ClientProductController(ClientProductService service) {
        this.service = service;
    }

    /**
     * GET /api/products?category=수학여행
     * 대분류 카테고리명으로 게시중·활성 상품 조회
     */
    @GetMapping
    public List<ProductDto> getByCategory(@RequestParam String category) {
        return service.getByCategory(category);
    }
}
