package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductSearchDto;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.admin.AdminProductService;
import com.nakshi.rohitour.service.admin.AdminProductVideoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService productService;
    private final AdminProductVideoService videoUploadService;
    private final UserRepository userRepository;

    public AdminProductController(AdminProductService productService,
                                  AdminProductVideoService videoUploadService,
                                  UserRepository userRepository) {
        this.productService = productService;
        this.videoUploadService = videoUploadService;
        this.userRepository = userRepository;
    }

    /* 목록 조회 */
    @GetMapping
    public PageResponse<ProductDto> findAll(
            @RequestParam int page,
            @RequestParam int size,
            ProductSearchDto searchDto
    ) {
        return productService.findAll(new PageRequest(page, size), searchDto);
    }

    /* 단건 조회 */
    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    /* 등록 — 저장 후 발급된 productId 반환 */
    @PostMapping
    public Map<String, Long> insert(@RequestBody ProductDto dto, Principal principal) {
        Long userId = userRepository.findByLoginId(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 정보를 찾을 수 없습니다."))
                .getUserId();
        dto.setCreatedBy(userId);
        dto.setUpdatedBy(userId);
        productService.insert(dto);
        return Map.of("productId", dto.getProductId());
    }

    /* 수정 */
    @PutMapping("/{id}")
    public int update(
            @PathVariable Long id,
            @RequestBody ProductDto dto,
            Principal principal
    ) {
        Long userId = userRepository.findByLoginId(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 정보를 찾을 수 없습니다."))
                .getUserId();
        dto.setProductId(id);
        dto.setUpdatedBy(userId);
        return productService.update(dto);
    }

    /* 유튜브 URL 저장 */
    @PatchMapping("/{id}/video-url")
    public void updateVideoUrl(@PathVariable Long id, @RequestBody Map<String, String> body) {
        productService.updateVideoUrl(id, body.get("videoUrl"));
    }

    /* 로컬 동영상 업로드 */
    @PostMapping(value = "/{id}/video", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadVideo(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) throws java.io.IOException {
        String videoPath = videoUploadService.uploadVideo(id, file);
        productService.updateVideoPath(id, videoPath);
    }

    /* 동영상 삭제 */
    @DeleteMapping("/{id}/video")
    public void deleteVideo(@PathVariable Long id) throws java.io.IOException {
        ProductDto product = productService.findById(id);
        if (product.getVideoPath() != null) {
            videoUploadService.deleteVideo(product.getVideoPath());
        }
        productService.clearVideo(id);
    }

    /* 비활성화 (논리 삭제) */
    @DeleteMapping("/{id}")
    public int deactivate(@PathVariable Long id) {
        return productService.deactivate(id);
    }

    /* 물리 삭제 */
    @DeleteMapping("/{id}/delete")
    public int delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}
