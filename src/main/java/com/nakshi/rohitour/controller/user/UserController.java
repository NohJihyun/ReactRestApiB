package com.nakshi.rohitour.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/*
 * 사용자 확인
 * 현재 로그인한 사용자의 정보를 조회하는 API
 * /api/users/me
 * 현재 로그인된 사용자 정보 반환
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }
        return new MeResponse(authentication.getName());
    }
    /* 토큰 ping 테스트
   @GetMapping("/ping")
    public String ping() {
        return "pong";
    } */
    public record MeResponse(String email) {}
}