package com.nakshi.rohitour.controller.user;

import com.nakshi.rohitour.dto.AgreeTermsRequest;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

        String loginId = authentication.getName();

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(authority -> authority.replace("ROLE_", ""))
                .orElse("USER");

        String name = userRepository.findByLoginId(loginId)
                .map(u -> u.getName())
                .orElse(null);

        return new MeResponse(loginId, name, role);
    }

    /**
     * 소셜 로그인 신규 사용자 약관 동의
     * POST /api/users/terms
     */
    @PostMapping("/terms")
    public ResponseEntity<Void> agreeTerms(
            @Valid @RequestBody AgreeTermsRequest request,
            Authentication authentication) {
        authService.agreeTerms(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    public record MeResponse(String loginId, String name, String role) {}
}