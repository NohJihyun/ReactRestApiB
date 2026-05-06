package com.nakshi.rohitour.repository.review;

import com.nakshi.rohitour.dto.ReviewCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReviewCommentMapper {
    List<ReviewCommentDto> findByReviewId(@Param("reviewId") Long reviewId);
    int countByReviewId(@Param("reviewId") Long reviewId);
    ReviewCommentDto findById(@Param("id") Long id);
    void insert(ReviewCommentDto dto);
    void update(ReviewCommentDto dto);
    void delete(@Param("id") Long id);
    void deleteByReviewId(@Param("reviewId") Long reviewId);
}
