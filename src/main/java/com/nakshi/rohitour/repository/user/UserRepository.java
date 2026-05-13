package com.nakshi.rohitour.repository.user;

import com.nakshi.rohitour.domain.user.AuthProvider;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.domain.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/*
 * findByEmail 호출처리
 */
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<T> 값 유/무 표현 래퍼클래스
    //로그인, 회원가입 중복체크, 소셜 로그인 매칭
    //NPE 예방처리 NULL 대신 Optional 컨테이너 돌려준다.
    Optional<User> findByEmail(String email);

    Optional<User> findByLoginId(String loginId);

    boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);

    Optional<User> findByNameAndPhoneAndProvider(String name, String phone, AuthProvider provider);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    Page<User> findByNameContainingIgnoreCaseOrLoginIdContainingIgnoreCase(String name, String loginId, Pageable pageable);

    long countByIsActive(Boolean isActive);

    long countByRole(UserRole role);

    @Query(value = """
        SELECT TO_CHAR(DATE_TRUNC('month', created_at), 'YYYY-MM') AS month,
               COUNT(*)::int AS cnt
        FROM users
        WHERE created_at >= NOW() - INTERVAL '12 months'
        GROUP BY month
        ORDER BY month
        """, nativeQuery = true)
    java.util.List<Object[]> findMonthlyRegistrations();

    @Query(value = """
        SELECT EXTRACT(YEAR FROM created_at)::int AS yr,
               COUNT(*)::int AS cnt
        FROM users
        WHERE created_at >= NOW() - INTERVAL '5 years'
        GROUP BY yr
        ORDER BY yr
        """, nativeQuery = true)
    java.util.List<Object[]> findYearlyRegistrations();

    @Query(value = "SELECT provider, COUNT(*)::int AS cnt FROM users GROUP BY provider ORDER BY cnt DESC", nativeQuery = true)
    java.util.List<Object[]> findCountByProvider();
}
