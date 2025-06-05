package org.example.xmut2.lovegoods.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 简单问答服务类
 * 该服务类负责处理用户关于物品存储和取出的相关问题
 * 使用@Service注解标记为Spring服务类
 */
@Service
public class SimpleQAService {
    
    // 创建日志记录器实例
    private static final Logger logger = LoggerFactory.getLogger(SimpleQAService.class);
    
    // 注入数据库操作模板
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 回答用户问题的主方法
     * 根据用户输入的问题，分析问题类型并返回相应的回答
     * 支持查询物品状态、类别和取物记录等功能
     *
     * @param question 用户输入的问题
     * @return 问题的回答
     */
    public String answerQuestion(String question) {
        try {
            // 转换为小写以便于匹配关键词
            String lowerQuestion = question.toLowerCase();
            logger.debug("处理问题: {}", lowerQuestion);
            
            // 检查是否询问物品状态
            if (lowerQuestion.contains("什么物品") || lowerQuestion.contains("有哪些物品") ||
                lowerQuestion.contains("库存") || lowerQuestion.contains("现有") ||
                lowerQuestion.contains("有什么") || lowerQuestion.contains("查看物品") ||
                lowerQuestion.contains("物品清单") || lowerQuestion.contains("物品列表") ||
                lowerQuestion.contains("存了") || lowerQuestion.contains("存放") ||
                lowerQuestion.contains("保管") || lowerQuestion.contains("寄存") ||
                lowerQuestion.contains("现有物品") || lowerQuestion.contains("当前物品") ||
                lowerQuestion.contains("物品情况") || lowerQuestion.contains("物品状态") ||
                lowerQuestion.contains("物品查询") || lowerQuestion.contains("物品明细") ||
                lowerQuestion.contains("物品报表") || lowerQuestion.contains("物品统计") ||
                lowerQuestion.contains("物品日志") || lowerQuestion.contains("物品追踪") ||
                lowerQuestion.contains("物品记录") || lowerQuestion.contains("物品历史") ||
                lowerQuestion.contains("物品信息") || lowerQuestion.contains("物品详情")) {
                return getItemsInStorage();
            }
            
            // 检查是否询问特定类别的物品
            if (lowerQuestion.contains("类别") || lowerQuestion.contains("分类") ||
                lowerQuestion.contains("类型") || lowerQuestion.contains("种类") ||
                lowerQuestion.contains("什么类") || lowerQuestion.contains("哪些类") ||
                lowerQuestion.contains("属于") || lowerQuestion.contains("归类") ||
                lowerQuestion.contains("分类情况") || lowerQuestion.contains("分类统计") ||
                lowerQuestion.contains("物品分类") || lowerQuestion.contains("物品类型") ||
                lowerQuestion.contains("分类查询") || lowerQuestion.contains("类型查询") ||
                lowerQuestion.contains("分类列表") || lowerQuestion.contains("类型列表") ||
                lowerQuestion.contains("分类明细") || lowerQuestion.contains("类型明细") ||
                lowerQuestion.contains("分类报表") || lowerQuestion.contains("类型报表") ||
                lowerQuestion.contains("分类统计") || lowerQuestion.contains("类型统计") ||
                lowerQuestion.contains("分类信息") || lowerQuestion.contains("类型信息")) {
                return getItemsByCategory(lowerQuestion);
            }
            
            // 检查是否询问取物记录
            if (lowerQuestion.contains("谁取") || lowerQuestion.contains("取了什么") ||
                lowerQuestion.contains("取物记录") || lowerQuestion.contains("取走") ||
                lowerQuestion.contains("最近取") || lowerQuestion.contains("取物历史") ||
                lowerQuestion.contains("取物情况") || lowerQuestion.contains("取物统计") ||
                lowerQuestion.contains("取物查询") || lowerQuestion.contains("取物明细") ||
                lowerQuestion.contains("取物清单") || lowerQuestion.contains("取物报表") ||
                lowerQuestion.contains("取物日志") || lowerQuestion.contains("取物追踪") ||
                lowerQuestion.contains("取物记录查询") || lowerQuestion.contains("取物历史记录") ||
                lowerQuestion.contains("取物信息") || lowerQuestion.contains("取物详情") ||
                lowerQuestion.contains("取物状态") || lowerQuestion.contains("取物情况") ||
                lowerQuestion.contains("取物统计") || lowerQuestion.contains("取物分析")) {
                return getTakeRecords();
            }
            
            // 如果无法理解问题，返回提示信息
            return "抱歉，我暂时无法理解您的问题。您可以询问：\n" +
                   "1. 库中有什么物品（库存、物品清单、存放情况、物品信息等）\n" +
                   "2. 谁取了什么物品（取物记录、取物历史、取物明细、取物信息等）";
        } catch (Exception e) {
            logger.error("处理问题时发生错误", e);
            throw new RuntimeException("处理问题时发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有存储中的物品信息
     * 查询数据库中状态为'STORED'的物品
     * 返回按类别分组的物品列表
     *
     * @return 格式化后的物品列表字符串
     */
    private String getItemsInStorage() {
        try {
            // SQL查询语句：获取所有存储中的物品信息，包括物品名称、类别、存储位置、所有者名称和创建时间
            String sql = "SELECT i.name, i.category, i.storage_location, u.name as owner_name, i.created_at " +
                        "FROM item i " +
                        "JOIN user u ON i.user_id = u.id " +
                        "WHERE i.status = 'STORED' " +
                        "ORDER BY i.category, i.name";
            
            logger.debug("执行SQL: {}", sql);
            List<Map<String, Object>> items = jdbcTemplate.queryForList(sql);
            
            if (items.isEmpty()) {
                return "目前库中没有存储的物品。";
            }
            
            // 构建响应字符串，按类别分组显示物品
            StringBuilder response = new StringBuilder("库中现有以下物品：\n");
            String currentCategory = null;
            
            for (Map<String, Object> item : items) {
                String category = (String) item.get("category");
                if (!category.equals(currentCategory)) {
                    response.append("\n【" + category + "】\n");
                    currentCategory = category;
                }
                
                response.append(String.format("- %s（存放位置：%s，存入者：%s，存入时间：%s）\n",
                        item.get("name"),
                        item.get("storage_location") != null ? item.get("storage_location") : "未指定",
                        item.get("owner_name"),
                        item.get("created_at")));
            }
            return response.toString();
        } catch (Exception e) {
            logger.error("获取存储物品时发生错误", e);
            throw new RuntimeException("获取存储物品时发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 获取特定类别的物品信息
     * 从问题中提取类别信息，查询该类别下的所有物品
     *
     * @param question 包含类别信息的问题
     * @return 格式化后的特定类别物品列表字符串
     */
    private String getItemsByCategory(String question) {
        try {
            // 从category表获取所有有效类别
            String categorySql = "SELECT name FROM category ORDER BY name";
            List<String> validCategories = jdbcTemplate.queryForList(categorySql, String.class);
            
            // 从问题中提取类别关键词
            String category = null;
            String lowerQuestion = question.toLowerCase();
            for (String cat : validCategories) {
                if (lowerQuestion.contains(cat.toLowerCase())) {
                    category = cat;
                    break;
                }
            }
            
            if (category == null) {
                return "请指定具体的类别（" + String.join("、", validCategories) + "）";
            }
            
            // SQL查询语句：获取指定类别的物品信息
            String sql = "SELECT i.name, i.storage_location, u.name as owner_name, i.created_at " +
                        "FROM item i " +
                        "JOIN user u ON i.user_id = u.id " +
                        "WHERE i.category = ? AND i.status = 'STORED' " +
                        "ORDER BY i.name";
            
            logger.debug("执行SQL: {}, 参数: {}", sql, category);
            List<Map<String, Object>> items = jdbcTemplate.queryForList(sql, category);
            
            if (items.isEmpty()) {
                return category + "类别下目前没有存储的物品。";
            }
            
            // 构建响应字符串
            StringBuilder response = new StringBuilder(category + "类别下有以下物品：\n");
            for (Map<String, Object> item : items) {
                response.append(String.format("- %s（存放位置：%s，存入者：%s，存入时间：%s）\n",
                        item.get("name"),
                        item.get("storage_location") != null ? item.get("storage_location") : "未指定",
                        item.get("owner_name"),
                        item.get("created_at")));
            }
            return response.toString();
        } catch (Exception e) {
            logger.error("获取类别物品时发生错误", e);
            throw new RuntimeException("获取类别物品时发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 获取最近的取物记录
     * 查询最近10条取物记录，包括物品信息、取物用户和时间
     *
     * @return 格式化后的取物记录列表字符串
     */
    private String getTakeRecords() {
        try {
            // SQL查询语句：获取最近10条取物记录
            String sql = "SELECT i.name, i.category, u.name as user_name, r.created_at " +
                        "FROM record r " +
                        "JOIN item i ON r.item_id = i.id " +
                        "JOIN user u ON r.user_id = u.id " +
                        "WHERE r.type = 'TAKE' " +
                        "ORDER BY r.created_at DESC " +
                        "LIMIT 10";
            
            logger.debug("执行SQL: {}", sql);
            List<Map<String, Object>> records = jdbcTemplate.queryForList(sql);
            
            if (records.isEmpty()) {
                return "目前没有取物记录。";
            }
            
            // 构建响应字符串
            StringBuilder response = new StringBuilder("最近的取物记录：\n");
            for (Map<String, Object> record : records) {
                response.append(String.format("- %s 取走了 %s（%s类，时间：%s）\n",
                        record.get("user_name"),
                        record.get("name"),
                        record.get("category"),
                        record.get("created_at")));
            }
            return response.toString();
        } catch (Exception e) {
            logger.error("获取取物记录时发生错误", e);
            throw new RuntimeException("获取取物记录时发生错误: " + e.getMessage(), e);
        }
    }
} 