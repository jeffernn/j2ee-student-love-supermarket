package org.example.xmut2.lovegoods.controller;

import org.example.xmut2.lovegoods.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * AI功能控制器
 */
@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    /**
     * 创建聊天会话
     */
    @PostMapping("/chat/sessions")
    public ResponseEntity<Map<String, String>> createChatSession() {
        String sessionId = aiService.createChatSession();
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }

    /**
     * 发送聊天消息
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(
            @RequestParam(required = false) String sessionId,
            @RequestBody Map<String, String> request) {
        
        String message = request.get("message");
        String response = aiService.chat(sessionId, message);
        
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("reply", response);
        return ResponseEntity.ok(responseMap);
    }

    /**
     * 清除聊天会话
     */
    @DeleteMapping("/chat/sessions/{sessionId}")
    public ResponseEntity<Map<String, String>> clearChatSession(@PathVariable String sessionId) {
        aiService.clearChatSession(sessionId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * 生成商品推荐语
     */
    @PostMapping("/product-recommendation")
    public ResponseEntity<Map<String, String>> generateProductRecommendation(@RequestBody Map<String, String> request) {
        String productDesc = request.get("productDesc");
        String recommendation = aiService.generateProductRecommendation(productDesc);
        
        Map<String, String> response = new HashMap<>();
        response.put("recommendation", recommendation);
        return ResponseEntity.ok(response);
    }

    /**
     * 智能回答商品问题
     */
    @PostMapping("/product-qa")
    public ResponseEntity<Map<String, String>> answerProductQuestion(@RequestBody Map<String, String> request) {
        String productInfo = request.get("productInfo");
        String question = request.get("question");
        String answer = aiService.answerProductQuestion(productInfo, question);
        
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return ResponseEntity.ok(response);
    }
} 