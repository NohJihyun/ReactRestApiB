package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.dto.ProductImageDto;
import com.nakshi.rohitour.service.admin.AdminProductImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/products/{productId}/images")
public class AdminProductImageController {

    private final AdminProductImageService imageService;

    public AdminProductImageController(AdminProductImageService imageService) {
        this.imageService = imageService;
    }

    /* 이미지 목록 */
    @GetMapping
    public List<ProductImageDto> getImages(@PathVariable Long productId) {
        return imageService.getImages(productId);
    }

    /* 이미지 업로드 */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImageDto upload(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") String imageType
    ) throws IOException {
        return imageService.uploadImage(productId, file, imageType);
    }

    /* 이미지 삭제 */
    @DeleteMapping("/{imageId}")
    public void delete(
            @PathVariable Long productId,
            @PathVariable Long imageId,
            @RequestParam("imagePath") String imagePath
    ) throws IOException {
        imageService.deleteImage(imageId, imagePath);
    }

    /* 이미지 순서 변경 */
    @PutMapping("/order")
    public void updateOrder(
            @PathVariable Long productId,
            @RequestBody List<Map<String, Object>> items
    ) {
        imageService.updateOrder(items);
    }
}
