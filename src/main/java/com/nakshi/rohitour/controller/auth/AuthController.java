package com.nakshi.rohitour.controller.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.LoginRequest;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;   // 🔥 이 줄 추가

    /**
     * 로그인 API
     * @RequestBody LoginRequest JSON -> loginRequest 객체로 변환
     * 반환 LoginResponse 응답용 DTO로 반환
     * 프론트에서 JSON 요청이 오면, 백엔드가 이를 객체로 변환하고, 처리 후 응답용 DTO 객체를 다시 JSON으로 변환해 반환한다.
     * @RestController return 스프링지원 JSON 반환
     * Controller에서 객체를 반환하면 Spring이 자동으로 JSON으로 변환해 응답한다.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        User user = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        // 첫토큰
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getRole().name()
        );

        //리프레시토큰
        String refreshToken = jwtUtil.generateRefreshToken(
                user.getEmail()
        );

        LoginResponse response = new LoginResponse(
                accessToken,
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }


}
