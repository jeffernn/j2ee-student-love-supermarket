package org.example.xmut2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
// 扫描 controller、service
public class Xmut2Application {
    public static void main(String[] args) {
        SpringApplication.run(Xmut2Application.class, args);
    }
}
