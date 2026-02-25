package com.nakshi.rohitour.config;

import com.nakshi.rohitour.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/*
 * 스프링 시큐리티
 * 기능 비활성화
 * 사용중인 기능만 적용
 * PasswordEncoder
 * 암호화 사용
 * Spring Boot 3 / Security 6 기준 코드
 * ✔ 보안 정책 설정
 * ✔ 세션 비활성화
 * ✔ JWT 필터 등록
 * ✔ URL 접근 제어
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //private final JwtUtil jwtUtil;
    // JWT 필터 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //
    private final SecurityErrorHandler securityErrorHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 컨트롤러 경로에 정확히 맞춤
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/reissue",
                                "/api/auth/logout"
                        ).permitAll()

                        // 예: 관리자
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                // 여기 추가: 401/403 응답 포맷 통일
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(securityErrorHandler)
                        .accessDeniedHandler(securityErrorHandler)
                )
                // new로 생성하지 말고, 주입된 Bean 사용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    // BCrypt 사용
    // 회원가입시 암호화 저장
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  모든 요청 허용 (지금은 JWT 안 쓰니까)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }*/
}
