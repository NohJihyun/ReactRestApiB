package com.nakshi.rohitour.service.review;

import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.repository.review.ReviewCommentMapper;
import com.nakshi.rohitour.repository.review.ReviewImageMapper;
import com.nakshi.rohitour.repository.review.ReviewMapper;
import com.nakshi.rohitour.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper imageMapper;
    private final ReviewCommentMapper commentMapper;
    private final UserRepository userRepository;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    public ReviewService(ReviewMapper reviewMapper, ReviewImageMapper imageMapper,
                         ReviewCommentMapper commentMapper, UserRepository userRepository) {
        this.reviewMapper = reviewMapper;
        this.imageMapper = imageMapper;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    /* ── 상품별 후기 목록 (공개) ── */
    public PageResponse<ReviewDto> getReviews(Long productId, int page, int size) {
        PageRequest pr = new PageRequest(page, size);
        List<ReviewDto> list = reviewMapper.findByProductId(productId, pr.getSize(), pr.getOffset());
        for (ReviewDto r : list) {
            r.setImages(imageMapper.findByReviewId(r.getId()));
        }
        int total = reviewMapper.countByProductId(productId);
        return new PageResponse<>(list, total, pr);
    }

    /* ── 상품 통계 (공개) ── */
    public ReviewStatsDto getStats(Long productId) {
        return reviewMapper.getStats(productId);
    }

    /* ── 메인 홈 최근 후기 (공개) ── */
    public List<ReviewDto> getRecentReviews(int limit) {
        List<ReviewDto> list = reviewMapper.findRecentPublished(limit);
        for (ReviewDto r : list) {
            List<ReviewImageDto> images = imageMapper.findByReviewId(r.getId());
            if (!images.isEmpty()) r.setImages(List.of(images.get(0)));
        }
        return list;
    }

    /* ── 내 후기 목록 (마이페이지) ── */
    public PageResponse<ReviewDto> getMyReviews(String loginId, int page, int size) {
        User user = getUser(loginId);
        PageRequest pr = new PageRequest(page, size);
        List<ReviewDto> list = reviewMapper.findByUserId(user.getUserId(), pr.getSize(), pr.getOffset());
        for (ReviewDto r : list) {
            r.setImages(imageMapper.findByReviewId(r.getId()));
        }
        int total = reviewMapper.countByUserId(user.getUserId());
        return new PageResponse<>(list, total, pr);
    }

    /* ── 후기 작성 ── */
    @Transactional
    public ReviewDto createReview(Long productId, String loginId, String writerType, int rating, String content) {
        User user = getUser(loginId);
        ReviewDto dto = new ReviewDto();
        dto.setProductId(productId);
        dto.setUserId(user.getUserId());
        dto.setWriterType(writerType);
        dto.setRating(rating);
        dto.setContent(content);
        reviewMapper.insert(dto);
        dto.setUserName(user.getName());
        dto.setImages(List.of());
        dto.setComments(List.of());
        return dto;
    }

    /* ── 후기 수정 ── */
    @Transactional
    public ReviewDto updateReview(Long reviewId, String loginId, String writerType, int rating, String content) {
        User user = getUser(loginId);
        ReviewDto existing = getReviewOrThrow(reviewId);
        checkOwner(existing, user);
        existing.setWriterType(writerType);
        existing.setRating(rating);
        existing.setContent(content);
        reviewMapper.update(existing);
        existing.setImages(imageMapper.findByReviewId(reviewId));
        return existing;
    }

    /* ── 후기 삭제 ── */
    @Transactional
    public void deleteReview(Long reviewId, String loginId, boolean isAdmin) throws IOException {
        User user = getUser(loginId);
        ReviewDto existing = getReviewOrThrow(reviewId);
        if (!isAdmin) checkOwner(existing, user);
        List<ReviewImageDto> images = imageMapper.findByReviewId(reviewId);
        for (ReviewImageDto img : images) deletePhysicalFile(img.getImagePath());
        reviewMapper.delete(reviewId);
    }

    /* ── 이미지 업로드 ── */
    @Transactional
    public ReviewImageDto uploadImage(Long reviewId, String loginId, MultipartFile file) throws IOException {
        User user = getUser(loginId);
        ReviewDto existing = getReviewOrThrow(reviewId);
        checkOwner(existing, user);
        String relativePath = saveFile(reviewId, file);
        int sortOrder = imageMapper.countByReviewId(reviewId);
        ReviewImageDto dto = new ReviewImageDto();
        dto.setReviewId(reviewId);
        dto.setImagePath(relativePath);
        dto.setSortOrder(sortOrder);
        imageMapper.insert(dto);
        return dto;
    }

    /* ── 이미지 삭제 ── */
    @Transactional
    public void deleteImage(Long imageId, String loginId, boolean isAdmin) throws IOException {
        ReviewImageDto img = imageMapper.findById(imageId);
        if (img == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!isAdmin) {
            User user = getUser(loginId);
            ReviewDto review = getReviewOrThrow(img.getReviewId());
            checkOwner(review, user);
        }
        deletePhysicalFile(img.getImagePath());
        imageMapper.delete(imageId);
    }

    /* ── 댓글 목록 (공개) ── */
    public List<ReviewCommentDto> getComments(Long reviewId) {
        return commentMapper.findByReviewId(reviewId);
    }

    /* ── 댓글 작성 ── */
    @Transactional
    public ReviewCommentDto addComment(Long reviewId, String loginId, String content) {
        User user = getUser(loginId);
        ReviewCommentDto dto = new ReviewCommentDto();
        dto.setReviewId(reviewId);
        dto.setUserId(user.getUserId());
        dto.setContent(content);
        commentMapper.insert(dto);
        dto.setUserName(user.getName());
        dto.setUserRole(user.getRole().name());
        return dto;
    }

    /* ── 댓글 수정 ── */
    @Transactional
    public ReviewCommentDto updateComment(Long commentId, String loginId, String content) {
        User user = getUser(loginId);
        ReviewCommentDto existing = getCommentOrThrow(commentId);
        if (!existing.getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글만 수정할 수 있습니다.");
        }
        existing.setContent(content);
        commentMapper.update(existing);
        return existing;
    }

    /* ── 댓글 삭제 ── */
    @Transactional
    public void deleteComment(Long commentId, String loginId, boolean isAdmin) {
        User user = getUser(loginId);
        ReviewCommentDto existing = getCommentOrThrow(commentId);
        if (!isAdmin && !existing.getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글만 삭제할 수 있습니다.");
        }
        commentMapper.delete(commentId);
    }

    /* ── 내부 헬퍼 ── */
    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private ReviewDto getReviewOrThrow(Long reviewId) {
        ReviewDto review = reviewMapper.findById(reviewId);
        if (review == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "후기를 찾을 수 없습니다.");
        return review;
    }

    private ReviewCommentDto getCommentOrThrow(Long commentId) {
        ReviewCommentDto comment = commentMapper.findById(commentId);
        if (comment == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");
        return comment;
    }

    private void checkOwner(ReviewDto review, User user) {
        if (!review.getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 후기만 수정/삭제할 수 있습니다.");
        }
    }

    private String saveFile(Long reviewId, MultipartFile file) throws IOException {
        String ext = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + ext;
        String relativePath = "/uploads/reviews/" + reviewId + "/" + fileName;
        Path fullPath = Paths.get(uploadBaseDir, "reviews", String.valueOf(reviewId), fileName);
        Files.createDirectories(fullPath.getParent());
        file.transferTo(fullPath);
        return relativePath;
    }

    private void deletePhysicalFile(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        String localPath = relativePath.replace("/uploads/", "");
        Path fullPath = Paths.get(uploadBaseDir, localPath);
        Files.deleteIfExists(fullPath);
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) return "jpg";
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
    }
}
