package com.nakshi.rohitour.config.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 소셜 로그인 시 provider에 강제 재인증 파라미터 추가
 * - Google: prompt=select_account (계정 선택 화면 강제)
 * - Kakao:  prompt=login          (카카오 로그인 화면 강제)
 * - Naver:  auth_type=reauthenticate (네이버 재인증 강제)
 *
 * 로그아웃 후 소셜 버튼 클릭 시 provider 세션이 남아 자동 로그인되는 현상 방지
 */
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final Pattern REGISTRATION_ID_PATTERN =
            Pattern.compile("/oauth2/authorization/([^/?]+)");

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository registrations) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                registrations, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        return addForceLoginParams(req, extractRegistrationId(request));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
        return addForceLoginParams(req, clientRegistrationId);
    }

    private OAuth2AuthorizationRequest addForceLoginParams(OAuth2AuthorizationRequest request,
                                                           String registrationId) {
        if (request == null || registrationId == null) return request;

        Map<String, Object> params = new HashMap<>(request.getAdditionalParameters());

        switch (registrationId) {
            case "google" -> params.put("prompt", "select_account");
            case "kakao"  -> params.put("prompt", "login");
            case "naver"  -> params.put("auth_type", "reauthenticate");
        }

        return OAuth2AuthorizationRequest.from(request)
                .additionalParameters(params)
                .build();
    }

    private String extractRegistrationId(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Matcher matcher = REGISTRATION_ID_PATTERN.matcher(uri);
        return matcher.find() ? matcher.group(1) : null;
    }
}
