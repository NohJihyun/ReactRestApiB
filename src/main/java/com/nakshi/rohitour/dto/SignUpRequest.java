package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    private String phone;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birth;

    @NotNull(message = "이용약관에 동의해주세요.")
    private Boolean agreedTerms;

    @NotNull(message = "개인정보 수집 및 이용에 동의해주세요.")
    private Boolean agreedPrivacy;

    @NotNull(message = "개인정보 제3자 제공에 동의해주세요.")
    private Boolean agreedThirdParty;

    private Boolean marketingAgreed;
}
