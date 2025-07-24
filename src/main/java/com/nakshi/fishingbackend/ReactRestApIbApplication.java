package com.nakshi.fishingbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nakshi.fishingbackend.repository.admin")
public class ReactRestApIbApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReactRestApIbApplication.class, args);
    }

}
