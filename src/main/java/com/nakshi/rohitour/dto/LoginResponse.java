package com.nakshi.rohitour.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
/*
 * 운영기준
 * accessToken api 호출 인증용
 * username 로그인 성공 후 ui 표시용
 * role 프론트에서 관리자 화면 분기용
 * AccessToken → Response Body
 * RefreshToken → HttpOnly Cookie
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String email;
    private String role;
    private String refreshToken; // 추가

}
