package com.hjh.muit.entity.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class ApiResponseDto {

    private int code;

    private String message;

    private Object data;

}
