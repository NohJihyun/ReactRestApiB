package com.nakshi.rohitour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
    "com.nakshi.rohitour.repository.admin",
    "com.nakshi.rohitour.repository.client",
    "com.nakshi.rohitour.repository.review",
    "com.nakshi.rohitour.repository.booking",
    "com.nakshi.rohitour.repository.inquiry"
})
public class ReactRestApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReactRestApiApplication.class, args);
    }

}
