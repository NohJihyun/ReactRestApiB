package com.nakshi.rohitour.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/*
 * 1️ MessageDigest.getInstance("SHA-256")
 * → SHA-256 해시 알고리즘 객체 생성
 * 2️ md.digest(bytes)
 * * → 문자열을 바이트 배열로 변환 후 해시 계산
 * 3 HexFormat.of().formatHex(digest)
 * → 바이트 배열을 16진수 문자열로 변환
 * DB에 저장 가능한 문자열 형태가 된다.
 */
public class TokenHashUtil {

    private TokenHashUtil() {}

    public static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not found", e);
        }
    }
}
