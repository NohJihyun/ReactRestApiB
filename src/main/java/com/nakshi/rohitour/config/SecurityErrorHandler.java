package com.nakshi.rohitour.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
/*
 *  Spring Security는 1jwt 필터 검사 2 인증/권환체트 3 통과 -> 컨트롤러 실행 4.  실패하면 컨트롤러까지 안감
 *  컨트롤러까지 못가면 GlobalExceptionHandler 실행되지 않음
 *  Security 단계에서 잘린 요청을 json으로 정리후 내려주는것
 *  "401 -> JSON으로 정리. 403 -> JSON으로 정리"
 *  “컨트롤러까지 못 들어간 401/403을 처리하는 것”
 * “시큐리티 레벨에서 발생한 401/403을 JSON 포맷으로 통일하는 전용 핸들러”
 */
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 인증 실패: 401
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = Map.of(
                "status", 401,
                "message", "인증이 필요합니다."
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    // 인가 실패: 403
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = Map.of(
                "status", 403,
                "message", "접근 권한이 없습니다."
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}