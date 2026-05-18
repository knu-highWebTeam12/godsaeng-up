package com.godsaeng.godsaeng_up.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();
    HttpStatus getStatus();
    String getMessage();
}

