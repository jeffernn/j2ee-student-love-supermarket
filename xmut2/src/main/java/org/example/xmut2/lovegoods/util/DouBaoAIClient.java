package org.example.xmut2.lovegoods.util;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.xmut2.lovegoods.config.DouBaoConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 豆包AI客户端工具类
 */
@Component
public class DouBaoAIClient {
    
    @Autowired
    private DouBaoConfig douBaoConfig;
    
    private final Map<String, List<Map<String, String>>> conversationHistory = new HashMap<>();
    private final Map<String, Long> sessionTimestamps = new HashMap<>();
    
    /**
     * 创建新的会话
     * 
     * @return 会话ID
     */
    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        conversationHistory.put(sessionId, new ArrayList<>());
        sessionTimestamps.put(sessionId, System.currentTimeMillis());
        return sessionId;
    }
    
    /**
     * 发送消息到豆包AI并获取回复
     * 
     * @param sessionId 会话ID
     * @param message 用户消息
     * @return AI回复
     */
    public String sendMessage(String sessionId, String message) {
        // 检查会话是否存在或过期
        if (!conversationHistory.containsKey(sessionId) || 
            System.currentTimeMillis() - sessionTimestamps.get(sessionId) > douBaoConfig.getSessionTimeout()) {
            sessionId = createSession();
        }
        
        // 更新会话时间戳
        sessionTimestamps.put(sessionId, System.currentTimeMillis());
        
        // 添加用户消息到历史记录
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        conversationHistory.get(sessionId).add(userMessage);
        
        try {
            // 准备请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", douBaoConfig.getDefaultModel());
            
            // 转换历史消息为JSON数组
            JSONArray messages = new JSONArray();
            for (Map<String, String> msg : conversationHistory.get(sessionId)) {
                JSONObject jsonMsg = new JSONObject();
                jsonMsg.put("role", msg.get("role"));
                jsonMsg.put("content", msg.get("content"));
                messages.put(jsonMsg);
            }
            requestBody.put("messages", messages);
            
            // 发送请求
            String response = sendRequest(requestBody.toString());
            JSONObject responseJson = new JSONObject(response);
            
            // 处理响应
            if (responseJson.has("error")) {
                return "AI服务出错: " + responseJson.getJSONObject("error").getString("message");
            }
            
            // 获取AI回复
            String aiReply = responseJson.getJSONObject("choices").getJSONObject("message").getString("content");
            
            // 添加AI回复到历史记录
            Map<String, String> aiMessage = new HashMap<>();
            aiMessage.put("role", "assistant");
            aiMessage.put("content", aiReply);
            conversationHistory.get(sessionId).add(aiMessage);
            
            return aiReply;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "与AI服务通信时发生错误: " + e.getMessage();
        }
    }
    
    /**
     * 发送HTTP请求到豆包AI API
     * 
     * @param requestBody 请求体JSON字符串
     * @return API响应
     * @throws IOException 网络异常
     * @throws ParseException 解析响应异常
     */
    private String sendRequest(String requestBody) throws IOException, ParseException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(douBaoConfig.getApiUrl() + "/chat/completions");
            
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + douBaoConfig.getApiKey());
            if (douBaoConfig.getApiKeyId() != null && !douBaoConfig.getApiKeyId().isEmpty()) {
                httpPost.setHeader("X-API-KEY-ID", douBaoConfig.getApiKeyId());
            }
            
            // 设置请求体
            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    
    /**
     * 清除指定会话的历史记录
     * 
     * @param sessionId 会话ID
     */
    public void clearSession(String sessionId) {
        if (conversationHistory.containsKey(sessionId)) {
            conversationHistory.get(sessionId).clear();
            sessionTimestamps.put(sessionId, System.currentTimeMillis());
        }
    }
} 