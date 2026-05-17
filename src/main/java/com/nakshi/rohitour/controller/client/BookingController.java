package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingRequestDto;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    /* 예약 신청 (로그인 필수) */
    @PostMapping
    public ResponseEntity<Map<String, String>> createBooking(
            @RequestBody BookingRequestDto req,
            Authentication authentication) {
        Long userId = resolveUserId(authentication);
        String bookingNumber = bookingService.createBooking(req, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("bookingNumber", bookingNumber));
    }

    /* 내 예약 목록 (로그인 필수) */
    @GetMapping("/me")
    public List<BookingDto> getMyBookings(Authentication authentication) {
        Long userId = resolveUserId(authentication);
        return bookingService.getMyBookings(userId);
    }

    /* 예약 수정 (PENDING만) */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequestDto req,
            Authentication authentication) {
        Long userId = resolveUserId(authentication);
        bookingService.updateBooking(id, req, userId);
        return ResponseEntity.ok().build();
    }

    /* 예약 삭제 (PENDING만) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = resolveUserId(authentication);
        bookingService.deleteBooking(id, userId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(Authentication authentication) {
        String name = authentication.getName();
        return userRepository.findByEmail(name)
                .or(() -> userRepository.findByLoginId(name))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getUserId();
    }
}
