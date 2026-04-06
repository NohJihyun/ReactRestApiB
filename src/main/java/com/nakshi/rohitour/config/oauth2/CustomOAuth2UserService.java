package com.nakshi.rohitour.config.oauth2;

import com.nakshi.rohitour.domain.user.AuthProvider;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

/**
 * 소셜 로그인 성공 후 사용자 정보 처리
 * 1. 공급자에서 받은 사용자 정보 파싱
 * 2. DB에 사용자 저장 또는 업데이트
 * 3. Spring Security가 사용할 OAuth2User 반환
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);  // 공급자 API로 사용자 정보 조회

        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // "naver"
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 공급자별 응답 파싱
        OAuthAttributes attrs = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // DB 저장 또는 업데이트
        User user = saveOrUpdateUser(attrs);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                attrs.getAttributes(),       // 내부 response 맵 (email, name, id 등)
                attrs.getNameAttributeKey()  // "id"
        );
    }

    private User saveOrUpdateUser(OAuthAttributes attrs) {
        // 1. 이 소셜 계정으로 기존에 로그인한 사용자 조회 (재방문)
        User user = userRepository.findByProviderAndProviderId(attrs.getProvider(), attrs.getProviderId())
                .orElse(null);

        if (user != null) {
            return userRepository.save(user);
        }

        // 2. 같은 이메일/loginId로 일반 가입된 사용자가 있는지 확인
        User existing = userRepository.findByEmail(attrs.getEmail())
                .or(() -> userRepository.findByLoginId(attrs.getEmail()))
                .orElse(null);

        if (existing != null && existing.getProvider() == AuthProvider.LOCAL) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("already_registered"),
                    "일반 회원가입으로 이미 가입이 되어 있습니다."
            );
        }

        // 3. 완전 신규 가입
        user = attrs.toUser(passwordEncoder.encode(UUID.randomUUID().toString()));
        return userRepository.save(user);
    }
}
