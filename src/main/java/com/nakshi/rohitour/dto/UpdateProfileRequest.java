package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record UpdateProfileRequest(
        @NotBlank String name,
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "연락처 형식이 올바르지 않습니다. (예: 010-1234-5678)")
        String phone,
        LocalDate birth
) {}
