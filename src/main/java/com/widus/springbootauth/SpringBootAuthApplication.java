package com.widus.springbootauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBootAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAuthApplication.class, args);
    }

}
