package org.example.xmut2.lovegoods.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义登录成功处理器
 * 用于处理用户登录成功后的重定向逻辑
 * 根据用户角色将用户重定向到不同的页面
 */
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 处理登录成功后的逻辑
     * 根据用户角色重定向到相应的页面
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param authentication 包含用户认证信息的对象
     * @throws IOException 如果重定向过程中发生IO异常
     * @throws ServletException 如果处理过程中发生Servlet异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取用户详情
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // 获取用户的第一个角色（假设用户只有一个角色）
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElseThrow();

        // 根据角色重定向到不同的页面
        if (role.equals("ROLE_ADMIN")) {
            // 管理员重定向到管理员仪表板
            response.sendRedirect("/admin/dashboard");
        } else if (role.equals("ROLE_STUDENT")) {
            // 学生重定向到学生仪表板
            response.sendRedirect("/student/dashboard");
        } else {
            // 其他角色重定向到首页
            response.sendRedirect("/");
        }
    }
}
