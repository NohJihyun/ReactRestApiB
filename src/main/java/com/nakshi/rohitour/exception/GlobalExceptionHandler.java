package com.nakshi.rohitour.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicate(DuplicateException e) {
        return Map.of(
                "status", 409,
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicateKey(DuplicateKeyException e) {
        return Map.of(
                "status", 409,
                "message", "이미 사용 중인 카테고리 코드입니다."
        );
    }

    // 핵심: ResponseStatusException은 상태코드 그대로 내려준다 (401/403/400 등)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException e) {

        int status = e.getStatusCode().value();

        Map<String, Object> body = Map.of(
                "status", status,
                "message", e.getReason()
        );

        return ResponseEntity.status(status).body(body);
    }

    // (권장) @Valid 검증 실패는 400으로
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException e) {

        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return Map.of(
                "status", 400,
                "message", msg
        );
    }

    // 진짜 서버 에러만 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception e, HttpServletRequest request) {

        log.error("Unhandled exception. path={}", request.getRequestURI(), e);

        return Map.of(
                "status", 500,
                "message", "서버 오류가 발생했습니다."
        );
    }
}