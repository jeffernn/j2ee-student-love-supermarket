package org.example.xmut2.lovegoods.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 豆包AI配置类
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "doubao.ai")
public class DouBaoConfig {
    /**
     * 豆包AI API密钥
     */
    private String apiKey;
    
    /**
     * 豆包AI API密钥ID
     */
    private String apiKeyId;
    
    /**
     * 豆包API基础URL
     */
    private String apiUrl = "https://api.doubao.com/v1";
    
    /**
     * 默认模型
     */
    private String defaultModel = "dbchat2";
    
    /**
     * 会话超时时间(ms)
     */
    private long sessionTimeout = 3600000; // 默认1小时

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiKeyId() {
        return apiKeyId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setApiKeyId(String apiKeyId) {
        this.apiKeyId = apiKeyId;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
} 