package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.ProductDto;
import com.nakshi.rohitour.dto.ProductSearchDto;
import com.nakshi.rohitour.dto.ReviewDto;
import com.nakshi.rohitour.dto.ReviewImageDto;
import com.nakshi.rohitour.exception.DuplicateException;
import com.nakshi.rohitour.repository.admin.*;
import com.nakshi.rohitour.repository.booking.BookingMapper;
import com.nakshi.rohitour.repository.review.ReviewCommentMapper;
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
    private final ReviewCommentMapper reviewCommentMapper;
    private final BookingMapper bookingMapper;
    private final AdminProductImageMapper productImageMapper;
    private final AdminProductFileMapper productFileMapper;
    private final AdminCruiseDetailMapper cruiseDetailMapper;
    private final AdminCruiseItineraryMapper cruiseItineraryMapper;
    private final AdminCruiseItineraryImageMapper cruiseItineraryImageMapper;
    private final AdminCruiseItineraryScheduleMapper cruiseItineraryScheduleMapper;
    private final AdminCruisePriceMapper cruisePriceMapper;
    private final AdminAirDetailMapper airDetailMapper;
    private final AdminAirItineraryMapper airItineraryMapper;
    private final AdminAirItineraryImageMapper airItineraryImageMapper;
    private final AdminAirItineraryScheduleMapper airItineraryScheduleMapper;
    private final AdminDomesticDetailMapper domesticDetailMapper;
    private final AdminDomesticItineraryMapper domesticItineraryMapper;
    private final AdminDomesticItineraryImageMapper domesticItineraryImageMapper;
    private final AdminDomesticItineraryScheduleMapper domesticItineraryScheduleMapper;
    private final AdminSchoolTripDetailMapper schoolTripDetailMapper;
    private final AdminSchoolTripItineraryMapper schoolTripItineraryMapper;
    private final AdminSchoolTripItineraryImageMapper schoolTripItineraryImageMapper;
    private final AdminSchoolTripItineraryScheduleMapper schoolTripItineraryScheduleMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminProductService(AdminProductMapper productMapper,
                               ReviewMapper reviewMapper,
                               ReviewImageMapper reviewImageMapper,
                               ReviewCommentMapper reviewCommentMapper,
                               BookingMapper bookingMapper,
                               AdminProductImageMapper productImageMapper,
                               AdminProductFileMapper productFileMapper,
                               AdminCruiseDetailMapper cruiseDetailMapper,
                               AdminCruiseItineraryMapper cruiseItineraryMapper,
                               AdminCruiseItineraryImageMapper cruiseItineraryImageMapper,
                               AdminCruiseItineraryScheduleMapper cruiseItineraryScheduleMapper,
                               AdminCruisePriceMapper cruisePriceMapper,
                               AdminAirDetailMapper airDetailMapper,
                               AdminAirItineraryMapper airItineraryMapper,
                               AdminAirItineraryImageMapper airItineraryImageMapper,
                               AdminAirItineraryScheduleMapper airItineraryScheduleMapper,
                               AdminDomesticDetailMapper domesticDetailMapper,
                               AdminDomesticItineraryMapper domesticItineraryMapper,
                               AdminDomesticItineraryImageMapper domesticItineraryImageMapper,
                               AdminDomesticItineraryScheduleMapper domesticItineraryScheduleMapper,
                               AdminSchoolTripDetailMapper schoolTripDetailMapper,
                               AdminSchoolTripItineraryMapper schoolTripItineraryMapper,
                               AdminSchoolTripItineraryImageMapper schoolTripItineraryImageMapper,
                               AdminSchoolTripItineraryScheduleMapper schoolTripItineraryScheduleMapper) {
        this.productMapper                     = productMapper;
        this.reviewMapper                      = reviewMapper;
        this.reviewImageMapper                 = reviewImageMapper;
        this.reviewCommentMapper               = reviewCommentMapper;
        this.bookingMapper                     = bookingMapper;
        this.productImageMapper                = productImageMapper;
        this.productFileMapper                 = productFileMapper;
        this.cruiseDetailMapper                = cruiseDetailMapper;
        this.cruiseItineraryMapper             = cruiseItineraryMapper;
        this.cruiseItineraryImageMapper        = cruiseItineraryImageMapper;
        this.cruiseItineraryScheduleMapper     = cruiseItineraryScheduleMapper;
        this.cruisePriceMapper                 = cruisePriceMapper;
        this.airDetailMapper                   = airDetailMapper;
        this.airItineraryMapper                = airItineraryMapper;
        this.airItineraryImageMapper           = airItineraryImageMapper;
        this.airItineraryScheduleMapper        = airItineraryScheduleMapper;
        this.domesticDetailMapper              = domesticDetailMapper;
        this.domesticItineraryMapper           = domesticItineraryMapper;
        this.domesticItineraryImageMapper      = domesticItineraryImageMapper;
        this.domesticItineraryScheduleMapper   = domesticItineraryScheduleMapper;
        this.schoolTripDetailMapper            = schoolTripDetailMapper;
        this.schoolTripItineraryMapper         = schoolTripItineraryMapper;
        this.schoolTripItineraryImageMapper    = schoolTripItineraryImageMapper;
        this.schoolTripItineraryScheduleMapper = schoolTripItineraryScheduleMapper;
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

    /* 물리 삭제 — 연관 데이터 전체 정리 후 삭제 */
    @Transactional
    public int delete(Long id) throws IOException {
        // 후기 이미지 파일 디스크 삭제
        List<ReviewDto> reviews = reviewMapper.findAllByProductId(id);
        for (ReviewDto review : reviews) {
            List<ReviewImageDto> images = reviewImageMapper.findByReviewId(review.getId());
            for (ReviewImageDto img : images) deletePhysicalFile(img.getImagePath());
        }

        // 후기 관련
        reviewCommentMapper.deleteByProductId(id);
        reviewImageMapper.deleteByProductId(id);
        reviewMapper.deleteAllByProductId(id);

        // 예약
        bookingMapper.deleteAllByProductId(id);

        // 크루즈
        cruiseItineraryScheduleMapper.deleteByProductId(id);
        cruiseItineraryImageMapper.deleteByProductId(id);
        cruiseItineraryMapper.deleteByProductId(id);
        cruiseDetailMapper.deleteByProductId(id);
        cruisePriceMapper.deleteByProductId(id);

        // 항공(국외)
        airItineraryScheduleMapper.deleteByProductId(id);
        airItineraryImageMapper.deleteByProductId(id);
        airItineraryMapper.deleteByProductId(id);
        airDetailMapper.deleteByProductId(id);

        // 국내
        domesticItineraryScheduleMapper.deleteByProductId(id);
        domesticItineraryImageMapper.deleteByProductId(id);
        domesticItineraryMapper.deleteByProductId(id);
        domesticDetailMapper.deleteByProductId(id);

        // 수학여행
        schoolTripItineraryScheduleMapper.deleteByProductId(id);
        schoolTripItineraryImageMapper.deleteByProductId(id);
        schoolTripItineraryMapper.deleteByProductId(id);
        schoolTripDetailMapper.deleteByProductId(id);

        // 상품 이미지/파일
        productImageMapper.deleteByProductId(id);
        productFileMapper.deleteByProductId(id);

        return productMapper.delete(id);
    }

    private void deletePhysicalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }
}
