package com.nakshi.rohitour.repository.booking;

import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingRequestDto;
import com.nakshi.rohitour.dto.BookingSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookingMapper {
    void insert(@Param("req") BookingRequestDto req,
                @Param("userId") Long userId,
                @Param("bookingNumber") String bookingNumber);

    List<BookingDto> findAll(BookingSearchDto search);
    int countAll(BookingSearchDto search);
    List<BookingDto> findAllForExport(BookingSearchDto search);

    BookingDto findById(Long bookingId);

    void updateStatus(@Param("bookingId") Long bookingId, @Param("status") String status);
    void updatePaymentStatus(@Param("bookingId") Long bookingId, @Param("paymentStatus") String paymentStatus, @Param("paymentMethod") String paymentMethod);
    void updateAdminMemo(@Param("bookingId") Long bookingId, @Param("adminMemo") String adminMemo);
    void markChecked(Long bookingId);

    int countUnchecked();

    List<BookingDto> findByUserId(Long userId);

    int sumReservedPeople();
    int sumConfirmedPeople();

    List<Map<String, Object>> countByStatus();
    List<Map<String, Object>> countByPaymentStatus();
    List<Map<String, Object>> countByProduct();

    void update(@Param("bookingId") Long bookingId,
                @Param("req") BookingRequestDto req,
                @Param("userId") Long userId);

    void delete(@Param("bookingId") Long bookingId,
                @Param("userId") Long userId);
}
