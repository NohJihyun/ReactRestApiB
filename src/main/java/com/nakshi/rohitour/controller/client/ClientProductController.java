package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.*;
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

    /** GET /api/products/{id}/cruise-itineraries */
    @GetMapping("/{id}/cruise-itineraries")
    public List<CruiseItineraryDto> getCruiseItineraries(@PathVariable Long id) {
        return service.getCruiseItineraries(id);
    }

    /** GET /api/products/{id}/cruise-details */
    @GetMapping("/{id}/cruise-details")
    public CruiseDetailDto getCruiseDetail(@PathVariable Long id) {
        return service.getCruiseDetail(id);
    }

    /** GET /api/products/{id}/cruise-prices */
    @GetMapping("/{id}/cruise-prices")
    public List<CruisePriceDto> getCruisePrices(@PathVariable Long id) {
        return service.getCruisePrices(id);
    }
}
