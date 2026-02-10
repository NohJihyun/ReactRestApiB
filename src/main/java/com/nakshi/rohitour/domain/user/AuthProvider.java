package com.nakshi.rohitour.domain.user;
 /*
  * 역할 사용자가 어떤 방식으로 로그인했는지 구분하는 기준 
  * enum 컴파일 타임에 체크 , 오타 원천 차단, QueryDsl 조건분기쉬움
  * 문자열로하면 provider = "kakao"
  * =>
  * enum 사용 provider == AuthProvider.KAKAO
  * 일반로그인, 소셜로그인 users 테이블 하나로 일반 로그인, 소셜로그인 전부 처리 가능하게 해주는 핵심 키
  */
public enum AuthProvider {
    LOCAL,
    KAKAO,
    NAVER,
    GOOGLE
}
