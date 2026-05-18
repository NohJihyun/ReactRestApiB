package com.nakshi.rohitour.repository.auth;

import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    long countByRevokedFalseAndExpiresAtAfter(java.time.LocalDateTime now);
}