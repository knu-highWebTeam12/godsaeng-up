package com.godsaeng.godsaeng_up.global.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// "공통 API 응답"
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        // "요청 성공 여부", example = "true"
        boolean success,

        // "성공 시 응답 데이터"
        T data,

        // "실패 시 에러 정보"
        ErrorResult error
        ) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, null);
    }

    public static ApiResponse<Void> fail(ErrorResult error) {
        return new ApiResponse<>(false, null, error);
    }
}
