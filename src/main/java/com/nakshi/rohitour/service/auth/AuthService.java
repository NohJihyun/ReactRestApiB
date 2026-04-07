package com.nakshi.rohitour.service.auth;

import com.nakshi.rohitour.config.JwtUtil;
import com.nakshi.rohitour.domain.auth.PasswordResetToken;
import com.nakshi.rohitour.domain.email.EmailVerification;
import com.nakshi.rohitour.domain.user.AuthProvider;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.domain.user.UserRole;
import com.nakshi.rohitour.dto.AgreeTermsRequest;
import com.nakshi.rohitour.dto.LoginResponse;
import com.nakshi.rohitour.dto.PasswordResetRequest;
import com.nakshi.rohitour.dto.PasswordResetSendRequest;
import com.nakshi.rohitour.dto.ReissueResponse;
import com.nakshi.rohitour.dto.SignUpRequest;
import com.nakshi.rohitour.repository.auth.PasswordResetTokenRepository;
import com.nakshi.rohitour.repository.auth.RefreshTokenRepository;
import com.nakshi.rohitour.repository.email.EmailVerificationRepository;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.email.EmailService;
import com.nakshi.rohitour.util.TokenHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nakshi.rohitour.domain.user.auth.RefreshToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Random;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    /**
     * 로그인 비즈니스 로직
     * AuthService는 로그인 시 사용자 조회와 검증을 담당하는 비즈니스 로직 계층이다.
     *
     * @Transactional 원자성 작업단위를 하나로 묶은것 => "다 성공하거나, 다 취소하거나 "
     */
    @Transactional
    public LoginResponse login(String email, String password) {

        // 1️ 사용자 조회 (loginId로 검색)
        User user = userRepository.findByLoginId(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));

        // 2️ 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
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

    /**
     * 이메일 인증코드 발송
     * - 기존 코드 삭제 후 새 코드 저장 (재발송 지원)
     * - 6자리 숫자 코드, 5분 만료
     */
    @Transactional
    public void sendEmailCode(String email) {
        // 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용된 인증 이메일입니다.");
        }

        // 기존 인증 레코드 삭제 (재발송 시 초기화)
        emailVerificationRepository.deleteAllByEmail(email);

        // 6자리 숫자 코드 생성
        String code = String.format("%06d", new Random().nextInt(1_000_000));

        EmailVerification verification = new EmailVerification(
                email,
                code,
                LocalDateTime.now().plusMinutes(5)
        );
        emailVerificationRepository.save(verification);

        // Amazon SES SMTP로 발송
        emailService.sendVerificationCode(email, code);
    }

    /**
     * 이메일 인증코드 검증
     * - 최근 발송된 코드 조회 → 일치 + 미만료 + 미인증 확인
     */
    @Transactional
    public void verifyEmailCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드를 먼저 발송해주세요."));

        if (!verification.isValid(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드가 올바르지 않거나 만료되었습니다.");
        }

        verification.markVerified();
    }

    /**
     * 회원가입
     * - 만 14세 이상 확인 (서버 기준 현재 날짜)
     * - 이메일 인증 완료 여부 확인
     * - 이메일/아이디 중복 확인
     * - 비밀번호 BCrypt 암호화 후 저장
     */
    @Transactional
    public void signUp(SignUpRequest request) {
        // 1. 필수 약관 동의 확인
        if (!Boolean.TRUE.equals(request.getAgreedTerms())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이용약관에 동의해주세요.");
        }
        if (!Boolean.TRUE.equals(request.getAgreedPrivacy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "개인정보 수집 및 이용에 동의해주세요.");
        }
        if (!Boolean.TRUE.equals(request.getAgreedThirdParty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "개인정보 제3자 제공에 동의해주세요.");
        }

        // 2. 만 14세 이상 확인 (서버 현재 날짜 기준)
        int age = Period.between(request.getBirth(), LocalDate.now()).getYears();
        if (age < 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "만 14세 이상만 가입하실 수 있습니다.");
        }

        // 2. 이메일 인증 완료 여부 확인
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증을 먼저 완료해주세요."));

        if (!verification.isVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다.");
        }

        // 3. 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "인증하시는 이메일이 이미 사용 중입니다.");
        }

        // 4. 아이디 중복 확인
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "작성 중인 아이디 이메일이 이미 사용 중입니다.");
        }

        // 5. 비밀번호 암호화 후 유저 저장
        LocalDateTime now = LocalDateTime.now();
        boolean marketingAgreed = Boolean.TRUE.equals(request.getMarketingAgreed());
        User user = User.builder()
                .name(request.getName())
                .loginId(request.getLoginId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .birth(request.getBirth())
                .phone(request.getPhone())
                .provider(AuthProvider.LOCAL)
                .role(UserRole.USER)
                .isActive(true)
                .agreedTermsAt(now)
                .agreedPrivacyAt(now)
                .agreedThirdPartyAt(now)
                .marketingAgreed(marketingAgreed)
                .marketingAgreedAt(marketingAgreed ? now : null)
                .build();

        userRepository.save(user);

        // 6. 사용한 인증 레코드 정리
        emailVerificationRepository.deleteAllByEmail(request.getEmail());
    }

    /**
     * 소셜 로그인 신규 사용자 약관 동의 처리
     */
    @Transactional
    public void agreeTerms(String email, AgreeTermsRequest request) {
        if (!Boolean.TRUE.equals(request.getAgreedTerms())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이용약관에 동의해주세요.");
        }
        if (!Boolean.TRUE.equals(request.getAgreedPrivacy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "개인정보 수집 및 이용에 동의해주세요.");
        }
        if (!Boolean.TRUE.equals(request.getAgreedThirdParty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "개인정보 제3자 제공에 동의해주세요.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        user.setAgreedTermsAt(now);
        user.setAgreedPrivacyAt(now);
        user.setAgreedThirdPartyAt(now);
        boolean marketing = Boolean.TRUE.equals(request.getMarketingAgreed());
        user.setMarketingAgreed(marketing);
        user.setMarketingAgreedAt(marketing ? now : null);
        userRepository.save(user);
    }

    /**
     * 아이디 찾기 - 이름 + 휴대폰으로 loginId 조회
     */
    public String findLoginId(String name, String phone) {
        User user = userRepository.findByNameAndPhoneAndProvider(name, phone, AuthProvider.LOCAL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 회원 정보가 없습니다."));
        return user.getLoginId();
    }

    /**
     * 비밀번호 재설정 이메일 발송
     * - loginId + 이름 + 휴대폰으로 본인 확인
     * - 확인되면 등록된 이메일로 재설정 링크 발송
     */
    @Transactional
    public void sendPasswordReset(PasswordResetSendRequest request) {
        // 1. loginId로 사용자 조회
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 회원 정보가 없습니다."));

        // 2. 이름 + 휴대폰 일치 확인
        if (!request.getName().equals(user.getName()) || !request.getPhone().equals(user.getPhone())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 회원 정보가 없습니다.");
        }

        // 3. 계정 활성 여부 확인
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비활성화된 계정입니다.");
        }

        // 4. 기존 재설정 토큰 삭제
        passwordResetTokenRepository.deleteAllByUser_UserId(user.getUserId());

        // 5. UUID 토큰 생성 → SHA-256 해시 저장
        String rawToken = UUID.randomUUID().toString();
        String hash = TokenHashUtil.sha256(rawToken);

        PasswordResetToken resetToken = new PasswordResetToken(
                hash,
                user,
                LocalDateTime.now().plusMinutes(30)
        );
        passwordResetTokenRepository.save(resetToken);

        // 6. 이메일 발송
        String resetLink = frontendUrl + "/password-reset?token=" + rawToken;
        emailService.sendPasswordResetLink(user.getEmail(), resetLink);
    }

    /**
     * 비밀번호 재설정
     * - 토큰 검증 → 새 비밀번호 저장
     */
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        String hash = TokenHashUtil.sha256(request.getToken());

        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 링크입니다."));

        if (!resetToken.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "만료되었거나 이미 사용된 링크입니다.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.markUsed();
    }
}