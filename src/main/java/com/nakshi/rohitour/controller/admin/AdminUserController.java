package com.nakshi.rohitour.controller.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.domain.user.UserRole;
import com.nakshi.rohitour.repository.admin.AdminProductMapper;
import com.nakshi.rohitour.repository.auth.RefreshTokenRepository;
import com.nakshi.rohitour.repository.booking.BookingMapper;
import com.nakshi.rohitour.repository.review.ReviewMapper;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.repository.visit.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final AdminProductMapper productMapper;
    private final ReviewMapper reviewMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BookingMapper bookingMapper;
    private final VisitMapper visitMapper;

    @Value("${app.super-admins}")
    private String superAdminsRaw;

    private Set<String> getSuperAdmins() {
        return Set.copyOf(Arrays.asList(superAdminsRaw.split(",")));
    }

    private void requireSuperAdmin(Principal principal) {
        if (principal == null || !getSuperAdmins().contains(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }

    @GetMapping("/users")
    public Map<String, Object> getUsers(
            Principal principal,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        requireSuperAdmin(principal);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = keyword.isBlank()
                ? userRepository.findAll(pageable)
                : userRepository.findByNameContainingIgnoreCaseOrLoginIdContainingIgnoreCase(keyword, keyword, pageable);

        List<UserDto> list = userPage.getContent().stream()
                .map(u -> new UserDto(
                        u.getUserId(), u.getLoginId(), u.getName(),
                        u.getRole().name(), u.getProvider().name(),
                        u.getIsActive(), u.getCreatedAt()))
                .toList();

        return Map.of(
                "list", list,
                "totalCount", userPage.getTotalElements(),
                "totalPage", userPage.getTotalPages()
        );
    }

    @PatchMapping("/users/{id}/role")
    public void changeRole(Principal principal, @PathVariable Long id, @RequestParam String role) {
        requireSuperAdmin(principal);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setRole(UserRole.valueOf(role));
        userRepository.save(user);
    }

    @GetMapping("/stats")
    public StatsDto getStats() {
        long totalUsers         = userRepository.count();
        long activeUsers        = userRepository.countByIsActive(true);
        long adminCount         = userRepository.countByRole(UserRole.ADMIN);
        long userCount          = userRepository.countByRole(UserRole.USER);
        int  totalProducts      = productMapper.countAllActive();
        int  publishedProducts  = productMapper.countProductsByStatus("PUBLISHED");
        int  draftProducts      = productMapper.countProductsByStatus("DRAFT");
        int  hiddenProducts     = productMapper.countProductsByStatus("HIDDEN");
        int  endedProducts      = productMapper.countProductsByStatus("ENDED");
        int  totalReviews       = reviewMapper.countAllReviews();
        int  publishedReviews   = reviewMapper.countReviewsByStatus("PUBLISHED");
        int  hiddenReviews      = reviewMapper.countReviewsByStatus("HIDDEN");
        long activeSessionCount = refreshTokenRepository.countByRevokedFalseAndExpiresAtAfter(LocalDateTime.now());
        int  reservedPeople     = bookingMapper.sumReservedPeople();
        int  confirmedPeople    = bookingMapper.sumConfirmedPeople();
        int  todayVisitors      = visitMapper.countToday();
        int  todayPcVisitors    = visitMapper.countTodayByDevice("PC");
        int  todayMobileVisitors = visitMapper.countTodayByDevice("MOBILE");

        List<Map<String, Object>> monthly = userRepository.findMonthlyRegistrations().stream()
                .map(row -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("month", row[0]);
                    m.put("count", ((Number) row[1]).intValue());
                    return m;
                }).toList();

        List<Map<String, Object>> yearly = userRepository.findYearlyRegistrations().stream()
                .map(row -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("year", ((Number) row[0]).intValue());
                    m.put("count", ((Number) row[1]).intValue());
                    return m;
                }).toList();

        List<Map<String, Object>> providerStats = userRepository.findCountByProvider().stream()
                .map(row -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("provider", row[0]);
                    m.put("count", ((Number) row[1]).intValue());
                    return m;
                }).toList();

        return new StatsDto(
                totalUsers, activeUsers, adminCount, userCount,
                totalProducts, publishedProducts, draftProducts, hiddenProducts, endedProducts,
                totalReviews, publishedReviews, hiddenReviews,
                activeSessionCount, monthly, yearly, providerStats,
                reservedPeople, confirmedPeople,
                todayVisitors, todayPcVisitors, todayMobileVisitors);
    }

    public record UserDto(
            Long userId,
            String loginId,
            String name,
            String role,
            String provider,
            Boolean isActive,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt) {}

    public record StatsDto(
            long totalUsers,
            long activeUsers,
            long adminCount,
            long userCount,
            int totalProducts,
            int publishedProducts,
            int draftProducts,
            int hiddenProducts,
            int endedProducts,
            int totalReviews,
            int publishedReviews,
            int hiddenReviews,
            long activeSessionCount,
            List<Map<String, Object>> monthlyRegistrations,
            List<Map<String, Object>> yearlyRegistrations,
            List<Map<String, Object>> providerStats,
            int reservedPeople,
            int confirmedPeople,
            int todayVisitors,
            int todayPcVisitors,
            int todayMobileVisitors) {}
}
