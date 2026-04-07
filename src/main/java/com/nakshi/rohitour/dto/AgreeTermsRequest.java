package com.nakshi.rohitour.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AgreeTermsRequest {
    @NotNull
    private Boolean agreedTerms;
    @NotNull
    private Boolean agreedPrivacy;
    @NotNull
    private Boolean agreedThirdParty;
    private Boolean marketingAgreed;
}
