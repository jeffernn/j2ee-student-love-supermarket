package org.example.xmut2.lovegoods.service.impl;

import org.example.xmut2.lovegoods.service.AIService;
import org.example.xmut2.lovegoods.util.DouBaoAIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AI服务实现
 */
@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private DouBaoAIClient douBaoAIClient;

    @Override
    public String createChatSession() {
        return douBaoAIClient.createSession();
    }

    @Override
    public String chat(String sessionId, String message) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = createChatSession();
        }
        return douBaoAIClient.sendMessage(sessionId, message);
    }

    @Override
    public void clearChatSession(String sessionId) {
        if (sessionId != null && !sessionId.trim().isEmpty()) {
            douBaoAIClient.clearSession(sessionId);
        }
    }

    @Override
    public String generateProductRecommendation(String productDesc) {
        String prompt = "请根据以下商品描述，用专业营销人员的口吻，生成一段精彩的商品推荐语，突出商品的优点和价值。商品描述如下：\n" + productDesc;
        return douBaoAIClient.sendMessage(createChatSession(), prompt);
    }

    @Override
    public String answerProductQuestion(String productInfo, String question) {
        String sessionId = createChatSession();
        String systemPrompt = "你是一位专业的商品顾问，你将根据以下商品信息，回答用户的问题。请确保回答准确、专业且有帮助。商品信息如下：\n" + productInfo;
        douBaoAIClient.sendMessage(sessionId, systemPrompt);
        return douBaoAIClient.sendMessage(sessionId, question);
    }
} 