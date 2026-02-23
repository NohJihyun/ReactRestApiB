package com.nakshi.rohitour.dto;

import lombok.Getter;

@Getter
public class ReissueResponse {

    private final String accessToken;

    // refresh 회전(rotate) 구현할 때만 쿠키 갱신용으로 사용
    private final String refreshToken;

    public ReissueResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}