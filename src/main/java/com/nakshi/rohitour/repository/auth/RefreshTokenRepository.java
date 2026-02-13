package com.nakshi.rohitour.repository.auth;

import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);
    // Spring Data JPA가 자동생성 쿼리
    void deleteAllByUser_UserId(Long userId);
}