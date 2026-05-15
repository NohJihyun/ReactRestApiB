package com.nakshi.rohitour.controller.admin;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingSearchDto;
import com.nakshi.rohitour.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    /* 예약 목록 */
    @GetMapping
    public PageResponse<BookingDto> getBookings(
            @RequestParam(defaultValue = "1")  int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) Long productId) {
        BookingSearchDto search = new BookingSearchDto();
        search.setPage(page);
        search.setSize(size);
        search.setKeyword(keyword);
        search.setStatus(status);
        search.setPaymentStatus(paymentStatus);
        search.setProductId(productId);
        return bookingService.getBookings(search);
    }

    /* 예약 단건 조회 */
    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    /* 미확인 신규 예약 수 (알림 뱃지) */
    @GetMapping("/new-count")
    public Map<String, Integer> getUncheckedCount() {
        return bookingService.getUncheckedCount();
    }

    /* 상태 변경 */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        bookingService.updateStatus(id, body.get("status"));
        return ResponseEntity.ok().build();
    }

    /* 관리자 메모 수정 */
    @PatchMapping("/{id}/memo")
    public ResponseEntity<Void> updateMemo(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        bookingService.updateAdminMemo(id, body.get("adminMemo"));
        return ResponseEntity.ok().build();
    }

    /* 확인 처리 (알림 뱃지 제거) */
    @PatchMapping("/{id}/check")
    public ResponseEntity<Void> markChecked(@PathVariable Long id) {
        bookingService.markChecked(id);
        return ResponseEntity.ok().build();
    }
}
