package com.hjh.muit.entity.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {

    private int code;
    private String message;
    private T data;

    public static <T> ApiResponseDto<T> success(String message, T data) {
        return ApiResponseDto.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDto<T> error(String message, HttpStatus status) {
        return ApiResponseDto.<T>builder()
                .code(status.value())
                .message(message)
                .data(null)
                .build();
    }

    public static <T> ApiResponseDto<T> error(String message, HttpStatus status, T data) {
        return ApiResponseDto.<T>builder()
                .code(status.value())
                .message(message)
                .data(data)
                .build();
    }
}
