package com.godsaeng.godsaeng_up.global.response.dto;

// "필드 단위 검증 실패 정보"
public record FieldValidationError(

        // "검증 실패 필드명", example = "member_id"
        String field,

        // "잘못 들어온 값 ", example = ""
        Object rejectedValue,

        // "검증 실패 사유", example = "아이디은는 필수입니다"
        String reason
        ) {
    public static FieldValidationError of(String field, Object rejectedValue, String reason) {
        return new FieldValidationError(field, rejectedValue, reason);
    }
}
