package com.nakshi.rohitour.controller.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.LoginRequest;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * 로그인 API
 * @RequestBody LoginRequest JSON -> loginRequest 객체로 변환
 * 반환 LoginResponse 응답용 DTO로 반환
 * 프론트에서 JSON 요청이 오면, 백엔드가 이를 객체로 변환하고, 처리 후 응답용 DTO 객체를 다시 JSON으로 변환해 반환한다.
 * @RestController return 스프링지원 JSON 반환
 * Controller에서 객체를 반환하면 Spring이 자동으로 JSON으로 변환해 응답한다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        //  서비스에서 검증 + 토큰 생성 + refreshToken DB 저장까지 끝냄
        LoginResponse loginResponse = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        //  RefreshToken은 HttpOnly Cookie로 내려보내기
        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // 운영(HTTPS)에서는 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        response.addCookie(refreshCookie);

        //  Body에는 AccessToken만 (refreshToken은 숨김)
        LoginResponse body = new LoginResponse(
                loginResponse.getAccessToken(),
                loginResponse.getEmail(),
                loginResponse.getRole(),
                null
        );

        return ResponseEntity.ok(body);
    }
}
