package com.godsaeng.godsaeng_up.global.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

// "에러 응답 정보"
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResult(

        // "업무/도메인 에러 코드", example = "USER_404"
        String code,

        // "에러 메시지", example = "사용자를 찾을수 없습니다"
        String message,

        // "입력값 검증 실패 목록"
        List<FieldValidationError> fieldErrors
        ) {
    public static ErrorResult of(String code , String message) {
        return new ErrorResult(code, message, null);
    }

    public static ErrorResult of(String code, String message, List<FieldValidationError> fieldErrors) {
        return new ErrorResult(code, message, fieldErrors);
    }

}
