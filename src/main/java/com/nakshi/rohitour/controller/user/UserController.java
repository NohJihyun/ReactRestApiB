package com.nakshi.rohitour.controller.user;

import com.nakshi.rohitour.domain.user.AuthProvider;
import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.dto.AgreeTermsRequest;
import com.nakshi.rohitour.dto.ChangePasswordRequest;
import com.nakshi.rohitour.dto.UpdateProfileRequest;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.file-share-admins:}")
    private String fileShareAdminsRaw;

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

        String subject = authentication.getName();

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(a -> a.replace("ROLE_", ""))
                .orElse("USER");

        User user = userRepository.findByEmail(subject)
                .or(() -> userRepository.findByLoginId(subject))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<String> fileShareAdmins = fileShareAdminsRaw.isBlank()
                ? Set.of()
                : Set.copyOf(Arrays.asList(fileShareAdminsRaw.split(",")));
        boolean isFileShareAdmin = fileShareAdmins.contains(user.getEmail())
                || fileShareAdmins.contains(user.getLoginId());

        return new MeResponse(
                user.getUserId(),
                user.getLoginId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirth(),
                user.getProvider().name(),
                role,
                user.getCreatedAt(),
                isFileShareAdmin
        );
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(
            @Valid @RequestBody UpdateProfileRequest req,
            Authentication authentication) {
        String subject = authentication.getName();
        User user = userRepository.findByEmail(subject)
                .or(() -> userRepository.findByLoginId(subject))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setName(req.name().trim());
        if (req.phone() != null && !req.phone().isBlank()) {
            user.setPhone(req.phone().trim());
        }
        user.setBirth(req.birth());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            Authentication authentication) {
        String subject = authentication.getName();
        User user = userRepository.findByEmail(subject)
                .or(() -> userRepository.findByLoginId(subject))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "소셜 로그인 계정은 비밀번호를 변경할 수 없습니다.");
        }
        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/terms")
    public ResponseEntity<Void> agreeTerms(
            @Valid @RequestBody AgreeTermsRequest request,
            Authentication authentication) {
        authService.agreeTerms(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    public record MeResponse(
            Long userId,
            String loginId,
            String name,
            String email,
            String phone,
            LocalDate birth,
            String provider,
            String role,
            LocalDateTime createdAt,
            boolean fileShareAdmin
    ) {}
}
