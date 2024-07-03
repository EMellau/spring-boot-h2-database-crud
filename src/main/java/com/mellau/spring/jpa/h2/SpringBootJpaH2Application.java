package com.mellau.spring.jpa.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.mellau.spring.jpa.h2.persistence")
public class SpringBootJpaH2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaH2Application.class, args);
    }

}
