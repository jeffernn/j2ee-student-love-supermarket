package org.example.xmut2.lovegoods.controller;

import org.example.xmut2.lovegoods.service.SimpleQAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单问答控制器
 * 该控制器处理与AI问答系统相关的API请求
 * 使用@RestController注解，所有方法返回的数据会自动转换为JSON格式
 * 使用@CrossOrigin注解允许跨域请求
 */
@RestController
@RequestMapping("/api/qa")  // API基础路径为/api/qa
@CrossOrigin(origins = "*", allowedHeaders = "*")  // 允许所有来源的跨域请求
public class SimpleQAController {
    
    // 创建日志记录器实例
    private static final Logger logger = LoggerFactory.getLogger(SimpleQAController.class);

    // 注入问答服务
    @Autowired
    private SimpleQAService qaService;
    
    // 注入数据库操作模板
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试数据库连接
     * 该方法用于验证数据库连接是否正常
     * 执行简单的SELECT 1查询来测试连接
     *
     * @return ResponseEntity包含测试结果的Map对象
     */
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, String>> testDatabase() {
        try {
            // 执行简单的数据库查询
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            Map<String, String> response = new HashMap<>();
            response.put("message", "数据库连接正常，测试结果：" + result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 记录错误日志
            logger.error("数据库连接测试失败", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "数据库连接测试失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 处理用户问题
     * 接收用户的问题并返回AI的回答
     * 使用POST方法，请求体为JSON格式
     *
     * @param request 包含用户问题的请求对象
     * @return ResponseEntity包含AI回答的Map对象
     */
    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody QuestionRequest request) {
        try {
            // 记录接收到的用户问题
            logger.info("收到问题: {}", request.getQuestion());
            // 调用服务处理问题
            String answer = qaService.answerQuestion(request.getQuestion());
            // 记录生成的回答
            logger.info("生成回答: {}", answer);
            
            Map<String, String> response = new HashMap<>();
            response.put("answer", answer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 记录错误日志
            logger.error("处理问题时发生错误", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "抱歉，处理您的问题时出现错误，请稍后再试。错误信息：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 问题请求内部类
     * 用于接收前端发送的问题数据
     */
    public static class QuestionRequest {
        private String question;  // 用户问题
        
        public String getQuestion() {
            return question;
        }
        
        public void setQuestion(String question) {
            this.question = question;
        }
    }
} 