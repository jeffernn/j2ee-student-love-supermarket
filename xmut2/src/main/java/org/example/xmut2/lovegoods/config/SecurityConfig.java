package org.example.xmut2.lovegoods.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Spring Security 配置类
 * 用于配置应用程序的安全策略，包括：
 * - 请求授权规则
 * - 登录认证配置
 * - 密码加密方式
 * - CSRF保护等
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 自定义登录成功处理器
     * 用于处理用户登录成功后的逻辑
     */
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 配置安全过滤器链
     * 定义请求的访问权限和认证方式
     * @param http HttpSecurity对象
     * @return 配置好的SecurityFilterChain
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 允许所有人访问的路径
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                // 需要ADMIN角色的路径
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 需要STUDENT角色的路径
                .requestMatchers("/student/**").hasRole("STUDENT")
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            // 配置表单登录
            .formLogin(form -> form
                // 自定义登录页面
                .loginPage("/login")
                // 使用自定义登录成功处理器
                .successHandler(authenticationSuccessHandler)
                // 允许所有人访问登录页面
                .permitAll()
            )
            // 配置登出功能
            .logout(logout -> logout.permitAll())
            // 禁用CSRF保护（仅用于开发环境，生产环境建议启用）
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * 配置密码编码器
     * 使用BCrypt算法进行密码加密
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
