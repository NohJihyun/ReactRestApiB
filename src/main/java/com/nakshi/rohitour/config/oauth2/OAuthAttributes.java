package com.nakshi.rohitour.config.oauth2;

import com.nakshi.rohitour.domain.user.AuthProvider;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.domain.user.UserRole;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 소셜 로그인 공급자별 사용자 정보 파싱
 * - 현재: 네이버
 * - 추후: 카카오, 구글 추가 예정
 */
@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;  // DefaultOAuth2User에 넘길 속성 맵
    private String nameAttributeKey;         // 속성 맵에서 사용자 식별자 키
    private String name;
    private String email;
    private String phone;
    private String providerId;
    private AuthProvider provider;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 로그인: " + registrationId);
    }

    /**
     * 네이버 응답 형식:
     * {
     *   "resultcode": "00",
     *   "message": "success",
     *   "response": { "id": "...", "email": "...", "name": "...", "mobile": "010-..." }
     * }
     * → userNameAttributeName = "response"
     * → 실제 사용자 정보는 response 안에 있음
     */
    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        OAuthAttributes attrs = new OAuthAttributes();
        attrs.attributes = response;
        attrs.nameAttributeKey = "id";
        attrs.name = (String) response.get("name");
        attrs.email = (String) response.get("email");
        attrs.phone = (String) response.get("mobile");  // 예: "010-1234-5678"
        attrs.providerId = (String) response.get("id");
        attrs.provider = AuthProvider.NAVER;
        return attrs;
    }

    /**
     * 카카오 응답 형식:
     * {
     *   "id": 123456789,
     *   "kakao_account": {
     *     "email": "user@kakao.com",
     *     "profile": { "nickname": "홍길동" }
     *   }
     * }
     * → userNameAttributeName = "id"
     * → 전화번호는 비즈앱 전용 scope이므로 수집 안 함
     */
    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        String providerId = String.valueOf(attributes.get("id"));

        // 비즈앱 심사 전에는 account_email scope 사용 불가 → null 대체값 사용
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
        if (email == null) {
            email = "kakao_" + providerId + "@kakao.local";
        }

        // SuccessHandler에서 getAttributes().get("email")로 접근할 수 있도록 email 추가
        Map<String, Object> normalizedAttributes = new HashMap<>(attributes);
        normalizedAttributes.put("email", email);

        OAuthAttributes attrs = new OAuthAttributes();
        attrs.attributes = normalizedAttributes;
        attrs.nameAttributeKey = "id";
        attrs.name = profile != null ? (String) profile.get("nickname") : "카카오사용자";
        attrs.email = email;
        attrs.phone = null;  // 카카오 전화번호는 비즈앱 전용
        attrs.providerId = providerId;
        attrs.provider = AuthProvider.KAKAO;
        return attrs;
    }

    public User toUser(String encodedPassword) {
        return User.builder()
                .loginId(email)       // 소셜 사용자는 이메일을 loginId로 사용
                .name(name)
                .email(email)
                .phone(phone)
                .password(encodedPassword)
                .provider(provider)
                .providerId(providerId)
                .role(UserRole.USER)
                .isActive(true)
                .marketingAgreed(false)
                .build();
    }
}
