package org.example.xmut2.lovegoods.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AI聊天页面控制器
 */
@Controller
@RequestMapping("/ai")
public class AIChatViewController {

    /**
     * 渲染AI聊天页面
     *
     * @return 视图名称
     */
    @GetMapping("/chat")
    public String showAIChatPage() {
        return "ai-chat";
    }
} 