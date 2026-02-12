package com.nakshi.rohitour.service.auth;

import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    //BCrypt 사용
    //PasswordEncoder 주입
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 비즈니스 로직
     * AuthService는 로그인 시 사용자 조회와 검증을 담당하는 비즈니스 로직 계층이다.
     */
    public User login(String email, String password) {

        // 1️ 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        // 2️ 비밀번호 검증 (아직 평문 비교)
        // BCrypt 비교로 변경
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3 계정 활성화 여부 확인
        if (!user.getIsActive()) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        return user;
    }
}
