package com.nakshi.rohitour.repository.booking;

import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingRequestDto;
import com.nakshi.rohitour.dto.BookingSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookingMapper {
    void insert(@Param("req") BookingRequestDto req,
                @Param("userId") Long userId,
                @Param("bookingNumber") String bookingNumber);

    List<BookingDto> findAll(BookingSearchDto search);
    int countAll(BookingSearchDto search);

    BookingDto findById(Long bookingId);

    void updateStatus(@Param("bookingId") Long bookingId, @Param("status") String status);
    void updateAdminMemo(@Param("bookingId") Long bookingId, @Param("adminMemo") String adminMemo);
    void markChecked(Long bookingId);

    int countUnchecked();

    List<BookingDto> findByUserId(Long userId);

    int sumReservedPeople();   // PENDING + CONFIRMED 인원 합계
    int sumConfirmedPeople();  // CONFIRMED 인원 합계

    void update(@Param("bookingId") Long bookingId,
                @Param("req") BookingRequestDto req,
                @Param("userId") Long userId);

    void delete(@Param("bookingId") Long bookingId,
                @Param("userId") Long userId);
}
