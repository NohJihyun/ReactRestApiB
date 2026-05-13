package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductSearchDto;
import com.nakshi.rohitour.dto.ReviewDto;
import com.nakshi.rohitour.dto.ReviewImageDto;
import com.nakshi.rohitour.exception.DuplicateException;
import com.nakshi.rohitour.repository.admin.AdminProductMapper;
import com.nakshi.rohitour.repository.review.ReviewImageMapper;
import com.nakshi.rohitour.repository.review.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminProductService {

    private final AdminProductMapper productMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminProductService(AdminProductMapper productMapper,
                               ReviewMapper reviewMapper,
                               ReviewImageMapper reviewImageMapper) {
        this.productMapper    = productMapper;
        this.reviewMapper     = reviewMapper;
        this.reviewImageMapper = reviewImageMapper;
    }

    /* 목록 + 검색 + 페이지네이션 */
    public PageResponse<ProductDto> findAll(PageRequest pageRequest, ProductSearchDto searchDto) {
        List<ProductDto> list = productMapper.findAll(
                pageRequest.getOffset(),
                pageRequest.getSize(),
                searchDto
        );
        int totalCount = productMapper.countAll(searchDto);
        return new PageResponse<>(list, totalCount, pageRequest);
    }

    /* 단건 조회 */
    public ProductDto findById(Long id) {
        return productMapper.findById(id);
    }

    /* 상품이 등록된 카테고리 ID 목록 */
    public List<Long> findDistinctCategoryIds() {
        return productMapper.findDistinctCategoryIds();
    }

    /* 등록 */
    public int insert(ProductDto dto) {
        int count = productMapper.countByCode(dto.getProductCode(), null);
        if (count > 0) {
            throw new DuplicateException("이미 사용 중인 상품 코드입니다.");
        }
        return productMapper.insert(dto);
    }

    /* 수정 */
    public int update(ProductDto dto) {
        return productMapper.update(dto);
    }

    /* 유튜브 URL 저장 */
    public void updateVideoUrl(Long productId, String videoUrl) {
        productMapper.updateVideoUrl(productId, videoUrl);
    }

    /* 로컬 동영상 경로 저장 */
    public void updateVideoPath(Long productId, String videoPath) {
        productMapper.updateVideoPath(productId, videoPath);
    }

    /* 동영상 삭제 */
    public void clearVideo(Long productId) {
        productMapper.clearVideo(productId);
    }

    /* 논리 삭제 (비활성화) */
    public int deactivate(Long id) {
        return productMapper.deactivate(id);
    }

    /* 물리 삭제 — 후기 이미지 파일 정리 후 삭제 */
    @Transactional
    public int delete(Long id) throws IOException {
        List<ReviewDto> reviews = reviewMapper.findAllByProductId(id);
        for (ReviewDto review : reviews) {
            List<ReviewImageDto> images = reviewImageMapper.findByReviewId(review.getId());
            for (ReviewImageDto img : images) deletePhysicalFile(img.getImagePath());
        }
        reviewMapper.deleteAllByProductId(id);
        return productMapper.delete(id);
    }

    private void deletePhysicalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }
}
