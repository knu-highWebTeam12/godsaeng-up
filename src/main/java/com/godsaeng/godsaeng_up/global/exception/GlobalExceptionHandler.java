package com.godsaeng.godsaeng_up.global.exception;

import com.godsaeng.godsaeng_up.global.response.dto.ApiResponse;
import com.godsaeng.godsaeng_up.global.response.dto.ErrorResult;
import com.godsaeng.godsaeng_up.global.response.dto.FieldValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직에서 발생한 CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("Business exception. code={}, message={}", errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        errorCode.getCode(),
                        errorCode.getMessage()
                )));
    }

    // @Valid 검증 실패
    // DTO에 선언된 Validation 어노테이션 실패시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldValidationError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldValidationError)
                .toList();

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        CommonErrorCode.INVALID_INPUT_VALUE.getCode(),
                        CommonErrorCode.INVALID_INPUT_VALUE.getMessage(),
                        fieldErrors
                )));
    }

    // @ModelAttribute 바인딩 실패(Form 데이터)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException e) {
        List<FieldValidationError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldValidationError)
                .toList();

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        CommonErrorCode.INVALID_INPUT_VALUE.getCode(),
                        CommonErrorCode.INVALID_INPUT_VALUE.getMessage(),
                        fieldErrors
                )));
    }

    // @Validated 검증 실패 (PathVariable, RequestParam 등)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        List<FieldValidationError> fieldErrors = e.getConstraintViolations()
                .stream()
                .map(violation -> FieldValidationError.of(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .toList();

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT_VALUE.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        CommonErrorCode.INVALID_INPUT_VALUE.getCode(),
                        CommonErrorCode.INVALID_INPUT_VALUE.getMessage(),
                        fieldErrors
                )));
    }

    // 지원하지 않는 HTTP 메서드 호출 시 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(CommonErrorCode.METHOD_NOT_ALLOWED.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        CommonErrorCode.METHOD_NOT_ALLOWED.getCode(),
                        CommonErrorCode.METHOD_NOT_ALLOWED.getMessage()
                )));
    }

    // 위쪽을 모두 통과한 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e,
            HttpServletRequest request) throws Exception {
        if (request.getRequestURI().startsWith("/actuator")) {
            throw e;
        }
        log.error("Unexpected exception", e);

        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.fail(ErrorResult.of(
                        CommonErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                )));
    }

    // FieldError → FieldValidationError 변환 메서드
    private FieldValidationError toFieldValidationError(FieldError fieldError) {
        return FieldValidationError.of(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
        );
    }
}