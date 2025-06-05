package org.example.xmut2.lovegoods.service;
import org.example.xmut2.lovegoods.model.TakeRecord;
import org.example.xmut2.lovegoods.model.Item;
import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.repository.ItemRepository;
import org.example.xmut2.lovegoods.repository.RecordRepository;
import org.example.xmut2.lovegoods.repository.TakeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物品服务类
 * 提供物品相关的业务操作，包括存储、检索、更新和统计等功能
 */
@Service
@Transactional
public class ItemService {

    /**
     * 物品数据访问对象
     */
    @Autowired
    private ItemRepository itemRepository;

    /**
     * 记录数据访问对象
     */
    @Autowired
    private RecordRepository recordRepository;

    /**
     * 取物记录数据访问对象
     */
    @Autowired
    private TakeRecordRepository takeRecordRepository;

    /**
     * 存储物品
     * @param item 要存储的物品
     * @return 存储后的物品（包含ID等信息）
     */
    public Item storeItem(Item item) {
        return itemRepository.save(item); // Hibernate 会校验所有非空字段
    }

    /**
     * 根据二维码检索物品
     * @param qrCode 物品二维码
     * @return 找到的物品，如果不存在则返回null
     */
    public Item retrieveItem(String qrCode) {
        return itemRepository.findByQrCode(qrCode);
    }

    /**
     * 更新物品信息
     * @param item 要更新的物品
     * @return 更新后的物品
     */
    public Item updateItem(Item item) {
        return itemRepository.save(item); // 使用 JPA 的 save 方法更新数据库
    }

    /**
     * 根据类别获取物品列表
     * @param category 物品类别
     * @return 该类别下的所有物品
     */
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    /**
     * 获取所有物品
     * @return 所有物品的列表
     */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * 获取所有已存储的物品
     * @return 状态为"STORED"的物品列表
     */
    public List<Item> getAllStoredItems() {
        return itemRepository.findByStatus("STORED");
    }

    /**
     * 根据类别获取已存储的物品
     * @param category 物品类别
     * @return 该类别下已存储的物品列表
     */
    public List<Item> getItemsByCategoryAndStored(String category) {
        return itemRepository.findByCategoryAndIsStored(category, true);
    }

    /**
     * 根据ID获取物品
     * @param id 物品ID
     * @return 找到的物品，如果不存在则返回null
     */
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    /**
     * 查询用户某类型的操作记录
     * @param userId 用户ID
     * @param type 操作类型（如"TAKE"）
     * @return 符合条件的记录列表
     */
    public List<TakeRecord> findRecordsByUserIdAndType(Long userId, String type) {
        return recordRepository.findByUserIdAndType(userId, type);
    }

    /**
     * 保存取物记录
     * @param item 被取出的物品
     * @param user 取物的用户
     */
    public void saveTakeRecord(Item item, User user) {
        TakeRecord takeRecord = new TakeRecord();
        takeRecord.setItemId(item.getId());
        takeRecord.setUserId(user.getId());
        takeRecord.setType("TAKE");
        takeRecord.setCreatedAt(LocalDateTime.now());
        recordRepository.save(takeRecord);
    }

    /**
     * 获取所有取物记录
     * @return 所有类型为"TAKE"的记录列表
     */
    public List<TakeRecord> getAllTakeRecords() {
        return recordRepository.findByType("TAKE");
    }

    /**
     * 根据年月获取取物记录
     * @param year 年份
     * @param month 月份
     * @return 指定年月的取物记录列表
     */
    public List<TakeRecord> getTakeRecordsByMonth(int year, int month) {
        return recordRepository.findByType("TAKE").stream()
                .filter(record -> {
                    LocalDateTime date = record.getCreatedAt();
                    return date.getYear() == year && date.getMonthValue() == month;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取按月统计的取物记录数量
     * @return Map<String, Long> 键为"年份-月份"，值为记录数量
     */
    public Map<String, Long> getMonthlyTakeRecordStats() {
        Map<String, Long> result = new HashMap<>();
        
        try {
            // 获取所有TAKE类型记录
            List<TakeRecord> records = recordRepository.findByType("TAKE");
            
            // 使用Java 8 Stream API进行分组统计
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            result = records.stream()
                    .collect(Collectors.groupingBy(
                            record -> record.getCreatedAt().format(formatter),
                            Collectors.counting()
                    ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 获取最近12个月的取物记录统计
     * @return 包含月份标签和记录数量的Map
     */
    public Map<String, Object> getRecentMonthlyStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取月份标签（最近12个月）
            List<String> monthLabels = new ArrayList<>();
            List<Long> recordCounts = new ArrayList<>();
            
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            
            // 准备最近12个月的月份标签
            for (int i = 11; i >= 0; i--) {
                LocalDateTime month = now.minusMonths(i);
                monthLabels.add(month.format(formatter));
            }
            
            // 获取按月统计数据
            Map<String, Long> monthlyStats = getMonthlyTakeRecordStats();
            
            // 组装数据
            for (String monthLabel : monthLabels) {
                recordCounts.add(monthlyStats.getOrDefault(monthLabel, 0L));
            }
            
            result.put("labels", monthLabels);
            result.put("counts", recordCounts);
        } catch (Exception e) {
            e.printStackTrace();
            // 确保至少返回空的数据结构
            result.put("labels", new ArrayList<>());
            result.put("counts", new ArrayList<>());
        }
        
        return result;
    }
}