package com.nakshi.rohitour.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import java.io.IOException;
import java.util.Collections;

/*
 * Spring Sequrity가 필터 체인을 거침
 * Authorization 헤더 확인
 * 토큰 검증
 * username 추출
 * SecurityContext에 인증 완료처리 저장
 * ✔ 모든 요청 가로채기
 * ✔ Authorization 헤더 확인
 * ✔ JWT 검증
 * ✔ SecurityContext에 인증 저장
 * ✔ permitAll 경로를 /api/auth/**로 정확히 맞추기
 * ✔ 필터에서 /api/auth/**는 아예 스킵(권장) permitAll reissue, login, logout 필터가 헤더 검사하다 이상한 토큰 들어오면
 *    불필요한 검증을 하게됨, => 실무에서 필터에서 스킵함
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    //  permitAll 엔드포인트는 필터 자체를 스킵(권장)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 이미 인증이 세팅돼 있으면 중복 세팅 안 함(안정성)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}

