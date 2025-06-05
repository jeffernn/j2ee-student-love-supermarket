package org.example.xmut2.lovegoods.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AI聊天页面控制器
 * 该控制器负责处理AI聊天相关的页面请求
 * 使用Spring MVC的@Controller注解标记为控制器类
 */
@Controller
@RequestMapping("/ai")  // 基础URL路径为/ai
public class AIChatViewController {

    /**
     * 渲染AI聊天页面
     * 该方法处理GET请求，访问路径为/ai/chat
     * 返回视图名称"ai-chat"，对应templates目录下的ai-chat.html模板
     *
     * @return 返回视图名称"ai-chat"，用于渲染AI聊天页面
     */
    @GetMapping("/chat")
    public String showAIChatPage() {
        return "ai-chat";
    }
} 