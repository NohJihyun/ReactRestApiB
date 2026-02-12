package com.nakshi.rohitour.repository.user;

import com.nakshi.rohitour.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * findByEmail 호출처리 
 */
public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<T> 값 유/무 표현 래퍼클래스
    //로그인, 회원가입 중복체크, 소셜 로그인 매칭
    //NPE 예방처리 NULL 대신 Optional 컨테이너 돌려준다.
    Optional<User> findByEmail(String email);

}
