package com.nakshi.rohitour.repository.email;

import com.nakshi.rohitour.domain.email.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    /** 가장 최근에 발송된 인증 레코드 1개 조회 */
    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    /** 해당 이메일의 모든 인증 레코드 삭제 (새 코드 발송 시 초기화) */
    void deleteAllByEmail(String email);
}
