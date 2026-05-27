package com.godsaeng.godsaeng_up.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        @Size(min = 4, max = 20)
        @Pattern(regexp = "^[a-zA-Z0-9]+$")
        String loginId,

        @NotBlank
        @Size(min = 8, max = 20)
        String password,

        @NotBlank
        @Size(min = 8, max = 20)
        String passwordConfirm,

        @NotBlank
        @Size(max = 12)
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$")
        String nickname
) {
}
