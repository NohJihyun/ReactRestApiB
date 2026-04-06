package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindLoginIdRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phone;
}
