package com.nakshi.rohitour.service.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.dto.ReissueResponse;
import com.nakshi.rohitour.repository.auth.RefreshTokenRepository;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.util.TokenHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
     *
     * @Transactional 원자성 작업단위를 하나로 묶은것 => "다 성공하거나, 다 취소하거나 "
     */
    @Transactional
    public LoginResponse login(String email, String password) {

        // 1️ 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 이메일입니다."));

        // 2️ 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }

        // 3️ 계정 활성 여부
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비활성화된 계정입니다.");
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
        // 비밀번호는 BCrypt 사용, => 사람이 입력하는 비밀번호 보호용
        // RefreshToken은 SHA-256 사용 => 서버가 만든 랜덤 토큰 보호용
        // 7️ 해시 생성 
        // BCrypt가 아니라 SHA-256 해시 저장 정책이므로 이 방식 유지
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
    //리이슈
    @Transactional
    public ReissueResponse reissue(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다.");
        }

        // 1) refreshToken JWT 유효성(서명/만료) 체크
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }

        // 2) refreshToken에서 email 추출
        String email = jwtUtil.extractUsername(refreshToken);

        // 3) 사용자 조회 + 활성 체크
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"존재하지 않는 사용자입니다."));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비활성화된 계정입니다.");
        }

        // 4) 쿠키 refreshToken을 sha256으로 변환 후 DB에서 조회
        String hash = TokenHashUtil.sha256(refreshToken);

        RefreshToken saved = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "저장된 리프레시 토큰이 없습니다."));

        // 5) 엔티티 내부 검증(만료/폐기)
        if (!saved.isValid()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "만료되었거나 폐기된 리프레시 토큰입니다.");
        }

        // 6) 새 AccessToken 발급
        String newAccessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getRole().name()
        );

        // 7) RefreshToken 회전(rotate) - 권장
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        String newHash = TokenHashUtil.sha256(newRefreshToken);

        // 1계정 1토큰 정책: 기존 토큰 삭제 후 새 토큰 저장
        refreshTokenRepository.deleteAllByUser_UserId(user.getUserId());

        RefreshToken newEntity = new RefreshToken(
                newHash,
                LocalDateTime.now().plusDays(7),
                user
        );
        refreshTokenRepository.save(newEntity);

        // 컨트롤러가 쿠키 갱신할 수 있게 refreshToken도 반환
        return new ReissueResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {

        // refreshToken이 없어도 로그아웃은 성공 처리(쿠키 삭제는 컨트롤러가 함)
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        // 유효하면 email 추출해서 DB 토큰 삭제
        if (jwtUtil.validateToken(refreshToken)) {
            String email = jwtUtil.extractUsername(refreshToken);

            userRepository.findByEmail(email).ifPresent(user ->
                    refreshTokenRepository.deleteAllByUser_UserId(user.getUserId())
            );
        }
    }
}