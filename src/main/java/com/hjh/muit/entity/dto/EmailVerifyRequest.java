package com.hjh.muit.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EmailVerifyRequest {

    @Email
    private String email;

    @Pattern(regexp="\\d{6}")
    private String code;
}

