package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.*;
import com.nakshi.rohitour.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ClientReviewController {

    private final ReviewService reviewService;

    /* ── 상품별 후기 목록 (공개) ── */
    @GetMapping("/api/products/{productId}/reviews")
    public PageResponse<ReviewDto> getReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        return reviewService.getReviews(productId, page, size);
    }

    /* ── 상품 통계 (공개) ── */
    @GetMapping("/api/products/{productId}/reviews/stats")
    public ReviewStatsDto getStats(@PathVariable Long productId) {
        return reviewService.getStats(productId);
    }

    /* ── 메인 홈 최근 후기 (공개) ── */
    @GetMapping("/api/reviews/recent")
    public List<ReviewDto> getRecentReviews(@RequestParam(defaultValue = "12") int limit) {
        return reviewService.getRecentReviews(limit);
    }

    /* ── 내 후기 목록 (마이페이지) ── */
    @GetMapping("/api/users/me/reviews")
    public PageResponse<ReviewDto> getMyReviews(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        requireAuth(authentication);
        return reviewService.getMyReviews(authentication.getName(), page, size);
    }

    /* ── 후기 작성 ── */
    @PostMapping("/api/products/{productId}/reviews")
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Long productId,
            @RequestParam String writerType,
            @RequestParam int rating,
            @RequestParam String content,
            Authentication authentication) {
        requireAuth(authentication);
        ReviewDto created = reviewService.createReview(productId, authentication.getName(), writerType, rating, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ── 후기 수정 ── */
    @PutMapping("/api/products/{productId}/reviews/{reviewId}")
    public ReviewDto updateReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestParam String writerType,
            @RequestParam int rating,
            @RequestParam String content,
            Authentication authentication) {
        requireAuth(authentication);
        return reviewService.updateReview(reviewId, authentication.getName(), writerType, rating, content);
    }

    /* ── 후기 삭제 ── */
    @DeleteMapping("/api/products/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            Authentication authentication) throws IOException {
        requireAuth(authentication);
        boolean isAdmin = hasAdminRole(authentication);
        reviewService.deleteReview(reviewId, authentication.getName(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    /* ── 이미지 업로드 ── */
    @PostMapping("/api/products/{productId}/reviews/{reviewId}/images")
    public ResponseEntity<ReviewImageDto> uploadImage(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {
        requireAuth(authentication);
        ReviewImageDto dto = reviewService.uploadImage(reviewId, authentication.getName(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /* ── 이미지 삭제 ── */
    @DeleteMapping("/api/products/{productId}/reviews/{reviewId}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @PathVariable Long imageId,
            Authentication authentication) throws IOException {
        requireAuth(authentication);
        boolean isAdmin = hasAdminRole(authentication);
        reviewService.deleteImage(imageId, authentication.getName(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    /* ── 댓글 목록 (공개) ── */
    @GetMapping("/api/products/{productId}/reviews/{reviewId}/comments")
    public List<ReviewCommentDto> getComments(
            @PathVariable Long productId,
            @PathVariable Long reviewId) {
        return reviewService.getComments(reviewId);
    }

    /* ── 댓글 작성 ── */
    @PostMapping("/api/products/{productId}/reviews/{reviewId}/comments")
    public ResponseEntity<ReviewCommentDto> addComment(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestParam String content,
            Authentication authentication) {
        requireAuth(authentication);
        ReviewCommentDto dto = reviewService.addComment(reviewId, authentication.getName(), content);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /* ── 댓글 수정 ── */
    @PutMapping("/api/products/{productId}/reviews/{reviewId}/comments/{commentId}")
    public ReviewCommentDto updateComment(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestParam String content,
            Authentication authentication) {
        requireAuth(authentication);
        return reviewService.updateComment(commentId, authentication.getName(), content);
    }

    /* ── 댓글 삭제 ── */
    @DeleteMapping("/api/products/{productId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            Authentication authentication) {
        requireAuth(authentication);
        boolean isAdmin = hasAdminRole(authentication);
        reviewService.deleteComment(commentId, authentication.getName(), isAdmin);
        return ResponseEntity.noContent().build();
    }

    /* ── 헬퍼 ── */
    private void requireAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
    }
}
