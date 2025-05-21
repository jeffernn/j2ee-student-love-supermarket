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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {


    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RecordRepository  recordRepository;
    @Autowired
    private TakeRecordRepository takeRecordRepository;



    public Item storeItem(Item item) {
        return itemRepository.save(item); // Hibernate 会校验所有非空字段
    }



    public Item retrieveItem(String qrCode) {
        return itemRepository.findByQrCode(qrCode);
    }
    public Item updateItem(Item item) {
        return itemRepository.save(item); // 使用 JPA 的 save 方法更新数据库
    }


    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    // 新增 getAllItems 方法
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Other item-related methods
    public List<Item> getAllStoredItems() {
        return itemRepository.findByStatus("STORED");
    }
    public List<Item> getItemsByCategoryAndStored(String category) {
        return itemRepository.findByCategoryAndIsStored(category, true);
    }
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    /**
     * 查询用户某类型的操作记录（如 TAKE）
     */
    public List<TakeRecord> findRecordsByUserIdAndType(Long userId, String type) {
        return recordRepository.findByUserIdAndType(userId, type);
    }

    /**
     * 保存取物记录
     */
    public void saveTakeRecord(Item item, User user) {
        TakeRecord takeRecord = new TakeRecord();
        takeRecord.setItemId(item.getId());
        takeRecord.setUserId(user.getId());
        takeRecord.setType("TAKE");
        takeRecord.setCreatedAt(LocalDateTime.now());
        recordRepository.save(takeRecord);
    }
    public List<TakeRecord> getAllTakeRecords() {
        return recordRepository.findByType("TAKE");
    }

    public List<TakeRecord> getTakeRecordsByMonth(String yearMonth) {
        return recordRepository.findByYearMonth(yearMonth);
    }
    public Map<String, Integer> getMonthlyTakeRecordCount() {
        List<TakeRecord> records = takeRecordRepository.findAll();

        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getCreatedAt().getMonth().toString(),
                        Collectors.summingInt(v -> 1)
                ));
    }




}