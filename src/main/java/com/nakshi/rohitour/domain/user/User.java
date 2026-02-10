package com.nakshi.rohitour.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/*
 *  역할
 *  users 객체를 통하여 테이블 1row에 자동연결 = JPA Entity
 *  “이 객체의 필드는 이 테이블의 컬럼이다”
 *  jpa 라이브러리가 이객체를 보고 테이블과 연결된 insert, select, update를 자동처리
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * LOCAL 로그인만 사용
     * 소셜 로그인은 NULL
     */
    @Column
    private String password;

    /**
     * LOCAL / KAKAO / NAVER / GOOGLE
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    /**
     * 소셜 로그인 고유 ID
     */
    @Column(name = "provider_id")
    private String providerId;

    /**
     * USER / ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
