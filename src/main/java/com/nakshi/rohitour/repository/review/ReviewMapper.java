package com.nakshi.rohitour.repository.review;

import com.nakshi.rohitour.dto.ReviewDto;
import com.nakshi.rohitour.dto.ReviewSearchDto;
import com.nakshi.rohitour.dto.ReviewStatsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReviewMapper {
    List<ReviewDto> findByProductId(@Param("productId") Long productId, @Param("size") int size, @Param("offset") int offset);
    int countByProductId(@Param("productId") Long productId);
    ReviewStatsDto getStats(@Param("productId") Long productId);
    List<ReviewDto> findRecentPublished(@Param("limit") int limit);
    List<ReviewDto> findByUserId(@Param("userId") Long userId, @Param("size") int size, @Param("offset") int offset);
    int countByUserId(@Param("userId") Long userId);
    ReviewDto findById(@Param("id") Long id);
    List<ReviewDto> findAll(ReviewSearchDto search);
    int countAll(ReviewSearchDto search);
    void insert(ReviewDto dto);
    void update(ReviewDto dto);
    void updateStatus(@Param("id") Long id, @Param("status") String status);
    void delete(@Param("id") Long id);
    List<ReviewDto> findAllByProductId(@Param("productId") Long productId);
    void deleteAllByProductId(@Param("productId") Long productId);

    int countAllReviews();
    int countReviewsByStatus(@Param("status") String status);
}
