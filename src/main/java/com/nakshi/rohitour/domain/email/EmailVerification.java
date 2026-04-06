package com.nakshi.rohitour.domain.email;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verifications")
@Getter
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public EmailVerification(String email, String code, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = false;
    }

    public void markVerified() {
        this.verified = true;
    }

    /** 코드가 만료되지 않았고 아직 인증 완료되지 않은 상태인지 */
    public boolean isValid(String inputCode) {
        return !this.verified
                && this.code.equals(inputCode)
                && LocalDateTime.now().isBefore(this.expiresAt);
    }

    /** 이메일 인증이 완료된 상태인지 (회원가입 직전 재확인) */
    public boolean isVerified() {
        return Boolean.TRUE.equals(this.verified);
    }
}
