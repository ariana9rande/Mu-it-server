package com.hjh.muit.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String loginId;

    private String password;

    private String passwordConfirm;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String provider;

    private String providerId;
}
