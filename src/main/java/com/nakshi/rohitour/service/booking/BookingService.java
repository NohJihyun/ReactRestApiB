package com.nakshi.rohitour.service.booking;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingRequestDto;
import com.nakshi.rohitour.dto.BookingSearchDto;
import com.nakshi.rohitour.repository.booking.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingMapper bookingMapper;

    @Transactional
    public void updateBooking(Long bookingId, BookingRequestDto req, Long userId) {
        BookingDto booking = bookingMapper.findById(bookingId);
        if (booking == null || !booking.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!"PENDING".equals(booking.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "신청 상태인 예약만 수정할 수 있습니다.");
        bookingMapper.update(bookingId, req, userId);
    }

    @Transactional
    public void deleteBooking(Long bookingId, Long userId) {
        BookingDto booking = bookingMapper.findById(bookingId);
        if (booking == null || !booking.getUserId().equals(userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!"PENDING".equals(booking.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "신청 상태인 예약만 삭제할 수 있습니다.");
        bookingMapper.delete(bookingId, userId);
    }

    @Transactional
    public String createBooking(BookingRequestDto req, Long userId) {
        String bookingNumber = generateBookingNumber();
        bookingMapper.insert(req, userId, bookingNumber);
        return bookingNumber;
    }

    public PageResponse<BookingDto> getBookings(BookingSearchDto search) {
        List<BookingDto> list = bookingMapper.findAll(search);
        int total             = bookingMapper.countAll(search);
        return new PageResponse<>(list, total, new PageRequest(search.getPage(), search.getSize()));
    }

    public BookingDto getBooking(Long bookingId) {
        BookingDto booking = bookingMapper.findById(bookingId);
        if (booking == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return booking;
    }

    @Transactional
    public void updateStatus(Long bookingId, String status) {
        bookingMapper.updateStatus(bookingId, status);
    }

    @Transactional
    public void updateAdminMemo(Long bookingId, String adminMemo) {
        bookingMapper.updateAdminMemo(bookingId, adminMemo);
    }

    @Transactional
    public void markChecked(Long bookingId) {
        bookingMapper.markChecked(bookingId);
    }

    public Map<String, Integer> getUncheckedCount() {
        return Map.of("count", bookingMapper.countUnchecked());
    }

    public List<BookingDto> getMyBookings(Long userId) {
        return bookingMapper.findByUserId(userId);
    }

    private String generateBookingNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "RHT-" + date + "-" + rand;
    }
}
