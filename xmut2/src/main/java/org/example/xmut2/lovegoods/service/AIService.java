package org.example.xmut2.lovegoods.service;

/**
 * AI服务接口
 */
public interface AIService {
    
    /**
     * 创建新的AI聊天会话
     * 
     * @return 会话ID
     */
    String createChatSession();
    
    /**
     * 发送聊天消息并获取AI回复
     * 
     * @param sessionId 会话ID，如果为null则创建新会话
     * @param message 用户消息
     * @return AI回复
     */
    String chat(String sessionId, String message);
    
    /**
     * 清除聊天会话历史记录
     * 
     * @param sessionId 会话ID
     */
    void clearChatSession(String sessionId);
    
    /**
     * 根据商品描述生成推荐语
     * 
     * @param productDesc 商品描述
     * @return 生成的推荐语
     */
    String generateProductRecommendation(String productDesc);
    
    /**
     * 智能回答商品相关问题
     * 
     * @param productInfo 商品信息
     * @param question 用户问题
     * @return AI回答
     */
    String answerProductQuestion(String productInfo, String question);
} 