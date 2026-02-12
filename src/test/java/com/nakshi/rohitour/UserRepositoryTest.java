package com.nakshi.rohitour;

import com.nakshi.rohitour.domain.user.User;
import com.nakshi.rohitour.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByEmailTest() {

        Optional<User> user = userRepository.findByEmail("test@test.com");

        if (user.isPresent()) {
            System.out.println("조회 성공: " + user.get().getEmail());
        } else {
            System.out.println("유저 없음");
        }
    }
}
