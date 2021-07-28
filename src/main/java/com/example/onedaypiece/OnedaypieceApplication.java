package com.example.onedaypiece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class OnedaypieceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnedaypieceApplication.class, args);
    }

}
