package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.InquiryDto;
import com.nakshi.rohitour.dto.InquirySearchDto;
import com.nakshi.rohitour.service.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    /* 전체 문의 목록 */
    @GetMapping
    public PageResponse<InquiryDto> getInquiries(
            @RequestParam(defaultValue = "1")  int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        InquirySearchDto search = new InquirySearchDto();
        search.setPage(page);
        search.setSize(size);
        search.setKeyword(keyword);
        search.setStatus(status);
        search.setCategory(category);
        return inquiryService.getInquiries(search);
    }

    /* 문의 단건 */
    @GetMapping("/{id}")
    public InquiryDto getInquiry(@PathVariable Long id) {
        return inquiryService.getInquiry(id, null, true);
    }

    /* 신규 문의 건수 (헤더 배지용) */
    @GetMapping("/new-count")
    public Map<String, Integer> getNewCount() {
        return inquiryService.getNewCount();
    }

    /* 문의 통계 (대시보드용) */
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return inquiryService.getStats();
    }

    /* 답변 등록/수정 */
    @PatchMapping("/{id}/answer")
    public ResponseEntity<Void> updateAnswer(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        inquiryService.updateAnswer(id, body.get("answer"));
        return ResponseEntity.ok().build();
    }

    /* 상태 변경 */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        inquiryService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    /* 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.ok().build();
    }
}
