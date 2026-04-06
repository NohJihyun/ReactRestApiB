package com.nakshi.rohitour.config.oauth2;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import com.nakshi.rohitour.repository.auth.RefreshTokenRepository;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.util.TokenHashUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 소셜 로그인 성공 시:
 * 1. JWT AccessToken + RefreshToken 발급
 * 2. RefreshToken → HttpOnly 쿠키
 * 3. 프론트엔드 /oauth/callback?token=... 으로 리다이렉트
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // CustomOAuth2UserService에서 attrs.attributes = 내부 response 맵으로 설정했으므로
        // getAttributes().get("email") 로 바로 접근 가능
        String email = (String) oAuth2User.getAttributes().get("email");

        User user = userRepository.findByEmail(email)
                .or(() -> userRepository.findByLoginId(email))
                .orElseThrow(() -> new RuntimeException("OAuth2 사용자를 찾을 수 없습니다."));

        // AccessToken 발급
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());

        // RefreshToken 발급 + DB 저장 (1계정 1토큰)
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        String hash = TokenHashUtil.sha256(refreshToken);
        refreshTokenRepository.deleteAllByUser_UserId(user.getUserId());
        refreshTokenRepository.save(new RefreshToken(hash, LocalDateTime.now().plusDays(7), user));

        // RefreshToken → HttpOnly 쿠키
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // 운영(HTTPS)에서는 true
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(refreshCookie);

        // 프론트엔드로 리다이렉트 (AccessToken을 URL 파라미터로 전달)
        String redirectUrl = frontendUrl + "/oauth/callback"
                + "?token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&role=" + user.getRole().name();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
