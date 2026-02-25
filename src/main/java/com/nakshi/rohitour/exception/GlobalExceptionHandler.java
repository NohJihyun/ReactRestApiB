package com.nakshi.rohitour.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;
/*
 * 컨트롤러/서비스에서 발생한 모든 예외를 담당한다.
 * 컨트롤러까지 들어온 요청에서 발생한 예외 처리
 * @Valid 검증실패 , 컨트롤러 진입 , DTO검증 실패 등
 * 진짜 서버에러 및 널포인트익셉션등 처리
 * 시큐리티 필터 단계에서 막힌 요청은 예외처리 하지 못함 별도처리
 */
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

        // 운영에서는 로그 꼭 남기는 걸 추천 (아래 둘 중 하나)
        // log.error("Unhandled exception. path={}", request.getRequestURI(), e);
        e.printStackTrace();

        return Map.of(
                "status", 500,
                "message", "서버 오류가 발생했습니다."
        );
    }
}