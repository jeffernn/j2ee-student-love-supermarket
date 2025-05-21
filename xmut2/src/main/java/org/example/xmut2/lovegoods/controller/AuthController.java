package org.example.xmut2.lovegoods.controller;

import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 显示登录页
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // 处理登录请求
//    @PostMapping("/login")
//    public String handleLogin(
//            @RequestParam String cardId,
//            @RequestParam String password,
//            Model model) {
//
//        User user = userService.findByCardId(cardId);
//
//        if (user == null || !userService.checkPassword(user, password)) {
//            model.addAttribute("error", "卡号或密码错误");
//            return "login";
//        }
//
//        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
//            return "redirect:/student/dashboard";
//        } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
//            return "redirect:/admin/dashboard";
//        }
//
//        model.addAttribute("error", "未知角色");
//        return "login";
//    }
}
