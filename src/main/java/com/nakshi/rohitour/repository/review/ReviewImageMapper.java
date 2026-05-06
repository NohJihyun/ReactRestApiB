package com.nakshi.rohitour.repository.review;

import com.nakshi.rohitour.dto.ReviewImageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReviewImageMapper {
    List<ReviewImageDto> findByReviewId(@Param("reviewId") Long reviewId);
    ReviewImageDto findById(@Param("id") Long id);
    int countByReviewId(@Param("reviewId") Long reviewId);
    void insert(ReviewImageDto dto);
    void delete(@Param("id") Long id);
    void deleteByReviewId(@Param("reviewId") Long reviewId);
}
