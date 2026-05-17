package com.nakshi.rohitour.controller.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.FindLoginIdRequest;
import com.nakshi.rohitour.dto.LoginRequest;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.dto.PasswordResetRequest;
import com.nakshi.rohitour.dto.PasswordResetSendRequest;
import com.nakshi.rohitour.dto.ReissueResponse;
import com.nakshi.rohitour.dto.SendEmailCodeRequest;
import com.nakshi.rohitour.dto.SignUpRequest;
import com.nakshi.rohitour.dto.VerifyEmailCodeRequest;
import java.util.Map;
import com.nakshi.rohitour.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
 *
 * 해당컨트롤러 역할
 * 보안/정책
 * 1.로그인 쿠키(토큰)세팅 책임
 * 2.리프레시토큰 처리
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
        // 리프레시토큰은 URL이 아닌 쿠키헤더에 담아 응답처리 => 재발급 토큰은 브라우저가 담당
        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // 운영(HTTPS)에서는 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        response.addCookie(refreshCookie);
        // 보안설계
        // Body에는 AccessToken만 (refreshToken은 숨김)
        // 리프레시토큰은 보안상 숨김처리하고, 접근토크만 body로 응답
        LoginResponse body = new LoginResponse(
                loginResponse.getAccessToken(),
                loginResponse.getEmail(),
                loginResponse.getRole(),
                null
        );

        return ResponseEntity.ok(body);
    }
    //POST /api/auth/reissue
    //쿠키 refreshToken을 읽어서 서비스에 넘김
    //서비스가 검증후 accessToken 및 필요시 새 refreshToken을 반환
    //새 refreshToken이 오면 쿠키 재세팅
    //응답 body에는 accessToken만 내려줌
    /**
     *   AccessToken 재발급
     * - refreshToken은 HttpOnly 쿠키에서 꺼내서 사용
     * - 응답 body에는 accessToken만 내려줌 (refreshToken은 쿠키로만 관리)
     * - AccessToken이 만료됐을 때 로그인 다시 안 하게 해주는 장치.
     */
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        // 서비스에서 refresh 검증 + 새 토큰 발급
        // (회전(rotate) 구현이면 newRefreshToken도 같이 내려주도록 설계)
        ReissueResponse reissue = authService.reissue(refreshToken);

        // 회전하는 구조라면: 새 refreshToken이 있을 때만 쿠키 갱신
        if (reissue.getRefreshToken() != null) {
            Cookie refreshCookie = new Cookie("refreshToken", reissue.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false); // 운영(HTTPS)에서는 true
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            response.addCookie(refreshCookie);
        }

        // 응답 body에는 accessToken만 (refreshToken은 null 처리해서 숨김)
        ReissueResponse body = new ReissueResponse(reissue.getAccessToken(), null);
        return ResponseEntity.ok(body);
    }
    /**
     *   로그아웃
     * - DB refreshToken 삭제
     * - refreshToken 쿠키 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        authService.logout(refreshToken);

        // 세션 무효화
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        // 쿠키 삭제
        Cookie deleteCookie = new Cookie("refreshToken", "");
        deleteCookie.setHttpOnly(true);
        deleteCookie.setSecure(false); // 운영(HTTPS)에서는 true
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(deleteCookie);

        return ResponseEntity.ok().build();
    }

    /**
     * 이메일 인증코드 발송
     * POST /api/auth/email/send
     */
    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        authService.sendEmailCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * 이메일 인증코드 검증
     * POST /api/auth/email/verify
     */
    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmailCode(@Valid @RequestBody VerifyEmailCodeRequest request) {
        authService.verifyEmailCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }

    /**
     * 회원가입
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 아이디 찾기 - 이름 + 휴대폰으로 loginId 반환
     * POST /api/auth/find-id
     */
    @PostMapping("/find-id")
    public ResponseEntity<Map<String, String>> findLoginId(@Valid @RequestBody FindLoginIdRequest request) {
        String loginId = authService.findLoginId(request.getName(), request.getPhone());
        return ResponseEntity.ok(Map.of("loginId", loginId));
    }

    /**
     * 비밀번호 재설정 이메일 발송
     * POST /api/auth/password/send
     */
    @PostMapping("/password/send")
    public ResponseEntity<Void> sendPasswordReset(@Valid @RequestBody PasswordResetSendRequest request) {
        authService.sendPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    /**
     * 비밀번호 재설정
     * POST /api/auth/password/reset
     */
    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
