package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.ReviewDto;
import com.nakshi.rohitour.dto.ReviewSearchDto;
import com.nakshi.rohitour.service.admin.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    /* ── 전체 후기 목록 (검색 + 페이지네이션) ── */
    @GetMapping
    public PageResponse<ReviewDto> getReviews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String writerType) {
        ReviewSearchDto search = new ReviewSearchDto();
        search.setPage(page);
        search.setSize(size);
        search.setKeyword(keyword);
        search.setStatus(status);
        search.setWriterType(writerType);
        return adminReviewService.getReviews(search);
    }

    /* ── 노출/숨김 처리 ── */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        adminReviewService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    /* ── 후기 삭제 ── */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) throws IOException {
        adminReviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
