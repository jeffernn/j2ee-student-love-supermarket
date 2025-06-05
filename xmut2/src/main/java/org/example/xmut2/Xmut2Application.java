package org.example.xmut2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Spring Boot 应用程序的主类
 * 用于启动和配置整个应用程序
 */
@SpringBootApplication
// 扫描 controller、service 等组件
public class Xmut2Application {
    /**
     * 应用程序的入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Xmut2Application.class, args);
    }
}
