package com.nakshi.rohitour.controller.client;

import com.nakshi.rohitour.dto.InquiryDto;
import com.nakshi.rohitour.dto.InquiryRequestDto;
import com.nakshi.rohitour.repository.user.UserRepository;
import com.nakshi.rohitour.service.inquiry.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class ClientInquiryController {

    private final InquiryService inquiryService;
    private final UserRepository userRepository;

    /* 문의 등록 (비로그인도 가능) */
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody InquiryRequestDto req,
                                       Authentication authentication) {
        Long userId = resolveUserIdOrNull(authentication);
        inquiryService.createInquiry(req, userId);
        return ResponseEntity.ok().build();
    }

    /* 내 문의 목록 */
    @GetMapping("/me")
    public List<InquiryDto> getMyInquiries(Authentication authentication) {
        return inquiryService.getMyInquiries(resolveUserId(authentication));
    }

    /* 문의 단건 조회 */
    @GetMapping("/{id}")
    public InquiryDto getInquiry(@PathVariable Long id, Authentication authentication) {
        return inquiryService.getInquiry(id, resolveUserId(authentication), false);
    }

    private Long resolveUserId(Authentication authentication) {
        String name = authentication.getName();
        return userRepository.findByEmail(name)
                .or(() -> userRepository.findByLoginId(name))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getUserId();
    }

    private Long resolveUserIdOrNull(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) return null;
        String name = authentication.getName();
        return userRepository.findByEmail(name)
                .or(() -> userRepository.findByLoginId(name))
                .map(u -> u.getUserId())
                .orElse(null);
    }
}
