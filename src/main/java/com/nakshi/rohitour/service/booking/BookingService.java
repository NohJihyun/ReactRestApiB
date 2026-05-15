package com.nakshi.rohitour.service.booking;

import com.nakshi.rohitour.common.paging.PageResponse;
import com.nakshi.rohitour.common.paging.PageRequest;
import com.nakshi.rohitour.dto.BookingDto;
import com.nakshi.rohitour.dto.BookingRequestDto;
import com.nakshi.rohitour.dto.BookingSearchDto;
import com.nakshi.rohitour.repository.booking.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
    public void updatePaymentStatus(Long bookingId, String paymentStatus, String paymentMethod) {
        bookingMapper.updatePaymentStatus(bookingId, paymentStatus, paymentMethod);
        if ("PAID".equals(paymentStatus)) {
            bookingMapper.updateStatus(bookingId, "CONFIRMED");
        }
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

    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> byStatus = new HashMap<>();
        for (Map<String, Object> row : bookingMapper.countByStatus()) {
            byStatus.put((String) row.get("status"), ((Number) row.get("cnt")).intValue());
        }
        Map<String, Integer> byPayment = new HashMap<>();
        for (Map<String, Object> row : bookingMapper.countByPaymentStatus()) {
            String ps = (String) row.get("payment_status");
            byPayment.put(ps, ((Number) row.get("cnt")).intValue());
            if ("PAID".equals(ps)) {
                byPayment.put("PAID_MANUAL", ((Number) row.get("manual_cnt")).intValue());
                byPayment.put("PAID_ONLINE", ((Number) row.get("online_cnt")).intValue());
            }
        }
        result.put("byStatus", byStatus);
        result.put("byPayment", byPayment);
        result.put("byProduct", bookingMapper.countByProduct());
        return result;
    }

    public List<BookingDto> getMyBookings(Long userId) {
        return bookingMapper.findByUserId(userId);
    }

    public byte[] exportToExcel(BookingSearchDto search) throws IOException {
        List<BookingDto> list = bookingMapper.findAllForExport(search);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("예약내역");
            sheet.setDefaultColumnWidth(15);

            // 기본 데이터 셀 스타일
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font dataFont = workbook.createFont();
            dataFont.setFontName("맑은 고딕");
            dataFont.setFontHeightInPoints((short) 10);
            dataStyle.setFont(dataFont);

            // 헤더 스타일 (파란 배경)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            Font headerFont = workbook.createFont();
            headerFont.setFontName("맑은 고딕");
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerStyle.setFont(headerFont);

            // 노란 배경 데이터 셀 스타일
            CellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            yellowStyle.setAlignment(HorizontalAlignment.CENTER);
            yellowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font yellowFont = workbook.createFont();
            yellowFont.setFontName("맑은 고딕");
            yellowFont.setFontHeightInPoints((short) 10);
            yellowFont.setBold(true);
            yellowStyle.setFont(yellowFont);

            // 타이틀 행 스타일
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyle.setAlignment(HorizontalAlignment.LEFT);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setWrapText(true);
            Font titleFont = workbook.createFont();
            titleFont.setFontName("맑은 고딕");
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 11);
            titleStyle.setFont(titleFont);

            // 상태 레이블
            Map<String, String> statusLabel = Map.of(
                "PENDING", "신청", "CONFIRMED", "확정", "COMPLETED", "완료", "CANCELLED", "취소");
            Map<String, String> paymentLabel = Map.of(
                "UNPAID", "미결제", "PAID", "결제완료", "REFUNDED", "환불");
            Map<String, String> methodLabel = Map.of(
                "MANUAL", "수기결제", "ONLINE", "온라인결제");

            int totalCols = 17;

            // 1행: 타이틀 안내 문구 (전체 컬럼 병합)
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(72);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(
                "로이투어 예약내역\n" +
                "노란색 강조 항목(연락처·예약상태·결제상태)을 우선 확인하세요.\n" +
                "예약 신청 내역을 한눈에 파악하실 수 있습니다.\n" +
                "유선 상담하시고 수기 결제하신 뒤, 결제가 완료되면 예약 목록 관리에서 [상세] 버튼을 클릭한 후 수기 결제 완료 처리하시면 로이투어 홈페이지에 고객 관리 데이터로 남습니다."
            );
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalCols - 1));

            // 2행: 컬럼 헤더 (전부 파란색)
            // 0:예약번호 1:대분류 2:상품명 3:예약자 4:연락처* 5:이메일
            // 6:성인 7:아동 8:유아 9:총인원 10:희망출발일
            // 11:예약상태* 12:결제상태* 13:결제방법 14:요청사항 15:관리자메모 16:신청일시
            String[] headers  = {"예약번호", "대분류", "상품명", "예약자", "연락처", "이메일",
                                  "성인", "아동", "유아", "총인원", "희망출발일",
                                  "예약상태", "결제상태", "결제방법", "요청사항", "관리자메모", "신청일시"};
            boolean[] isYellow = {false, false, false, false, true, false,
                                   false, false, false, false, false,
                                   true, true, false, false, false, false};

            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(20);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 2;
            for (BookingDto b : list) {
                Row row = sheet.createRow(rowIdx++);
                String[] values = {
                    b.getBookingNumber(),
                    b.getRootCategoryName() != null ? b.getRootCategoryName() : "",
                    b.getProductName(),
                    b.getName(),
                    b.getPhone(),
                    b.getEmail(),
                    String.valueOf(b.getAdultCount()),
                    String.valueOf(b.getChildCount()),
                    String.valueOf(b.getInfantCount()),
                    String.valueOf(b.getAdultCount() + b.getChildCount() + b.getInfantCount()),
                    b.getDesiredDepartureAt() != null ? b.getDesiredDepartureAt().toString() : "",
                    statusLabel.getOrDefault(b.getStatus(), b.getStatus()),
                    paymentLabel.getOrDefault(b.getPaymentStatus(), b.getPaymentStatus()),
                    b.getPaymentMethod() != null ? methodLabel.getOrDefault(b.getPaymentMethod(), b.getPaymentMethod()) : "",
                    b.getRequestMemo() != null ? b.getRequestMemo() : "",
                    b.getAdminMemo() != null ? b.getAdminMemo() : "",
                    b.getCreatedAt() != null ? b.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "",
                };
                row.setHeightInPoints(18);
                for (int i = 0; i < values.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(values[i]);
                    cell.setCellStyle(isYellow[i] ? yellowStyle : dataStyle);
                }
            }

            // 컬럼 너비 지정
            int[] colWidths = {18, 16, 28, 12, 16, 30, 6, 6, 6, 8, 14, 10, 12, 12, 30, 30, 20};
            for (int i = 0; i < colWidths.length; i++) {
                sheet.setColumnWidth(i, colWidths[i] * 256);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String generateBookingNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "RHT-" + date + "-" + rand;
    }
}
