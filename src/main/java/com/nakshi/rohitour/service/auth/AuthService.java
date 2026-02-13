package com.nakshi.rohitour.service.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.repository.auth.RefreshTokenRepository;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.util.TokenHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    //BCrypt 사용
    //PasswordEncoder 주입
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    /**
     * 로그인 비즈니스 로직
     * AuthService는 로그인 시 사용자 조회와 검증을 담당하는 비즈니스 로직 계층이다.
     */
    @Transactional
    public LoginResponse login(String email, String password) {

        // 1️ 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        // 2️ 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3️ 계정 활성 여부
        if (!user.getIsActive()) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        // 4️ 기존 RefreshToken 삭제 (1계정 1토큰 정책)
        refreshTokenRepository.deleteAllByUser_UserId(user.getUserId());

        // 5️ AccessToken 생성
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getRole().name()
        );

        // 6️ RefreshToken 생성
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // 7️ 해시 생성
        String hash = TokenHashUtil.sha256(refreshToken);

        // 8 DB 저장
        RefreshToken entity = new RefreshToken(
                hash,
                LocalDateTime.now().plusDays(7),
                user
        );

        refreshTokenRepository.save(entity);

        // 9️ 기존 LoginResponse 사용
        return new LoginResponse(
                accessToken,
                user.getEmail(),
                user.getRole().name(),
                refreshToken   // 여기 추가 (쿠키용)
        );
    }
}