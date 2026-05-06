package com.nakshi.rohitour.service.admin;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.ReviewDto;
import com.nakshi.rohitour.dto.ReviewImageDto;
import com.nakshi.rohitour.dto.ReviewSearchDto;
import com.nakshi.rohitour.repository.review.ReviewCommentMapper;
import com.nakshi.rohitour.repository.review.ReviewImageMapper;
import com.nakshi.rohitour.repository.review.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper imageMapper;
    private final ReviewCommentMapper commentMapper;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public AdminReviewService(ReviewMapper reviewMapper, ReviewImageMapper imageMapper,
                              ReviewCommentMapper commentMapper) {
        this.reviewMapper = reviewMapper;
        this.imageMapper = imageMapper;
        this.commentMapper = commentMapper;
    }

    /* ── 전체 후기 목록 (검색 + 페이지네이션) ── */
    public PageResponse<ReviewDto> getReviews(ReviewSearchDto search) {
        List<ReviewDto> list = reviewMapper.findAll(search);
        for (ReviewDto r : list) {
            r.setImages(imageMapper.findByReviewId(r.getId()));
        }
        int total = reviewMapper.countAll(search);
        com.nakshi.rohitour.common.paging.PageRequest pr =
                new com.nakshi.rohitour.common.paging.PageRequest(search.getPage(), search.getSize());
        return new PageResponse<>(list, total, pr);
    }

    /* ── 노출/숨김 처리 ── */
    @Transactional
    public void updateStatus(Long reviewId, String status) {
        ReviewDto existing = reviewMapper.findById(reviewId);
        if (existing == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        reviewMapper.updateStatus(reviewId, status);
    }

    /* ── 후기 삭제 ── */
    @Transactional
    public void deleteReview(Long reviewId) throws IOException {
        ReviewDto existing = reviewMapper.findById(reviewId);
        if (existing == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<ReviewImageDto> images = imageMapper.findByReviewId(reviewId);
        for (ReviewImageDto img : images) deletePhysicalFile(img.getImagePath());
        reviewMapper.delete(reviewId);
    }

    private void deletePhysicalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }
}
