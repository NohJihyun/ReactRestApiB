package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductFileDto;
import com.nakshi.rohitour.dto.ProductImageDto;
import com.nakshi.rohitour.service.client.ClientProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ClientProductController {

    private final ClientProductService service;

    public ClientProductController(ClientProductService service) {
        this.service = service;
    }

    /** GET /api/products?category=수학여행 */
    @GetMapping
    public List<ProductDto> getByCategory(@RequestParam String category) {
        return service.getByCategory(category);
    }

    /** GET /api/products/{id} */
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        ProductDto product = service.getById(id);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");
        return product;
    }

    /** GET /api/products/{id}/images */
    @GetMapping("/{id}/images")
    public List<ProductImageDto> getImages(@PathVariable Long id) {
        return service.getImages(id);
    }

    /** GET /api/products/{id}/files */
    @GetMapping("/{id}/files")
    public List<ProductFileDto> getFiles(@PathVariable Long id) {
        return service.getFiles(id);
    }
}
