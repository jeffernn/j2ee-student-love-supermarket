package org.example.xmut2.lovegoods.controller;

import com.google.gson.Gson;
import org.example.xmut2.lovegoods.model.Item;
import org.example.xmut2.lovegoods.model.TakeRecord;
import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.service.ItemService;
import org.example.xmut2.lovegoods.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;



    // 管理员首页
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Item> items = itemService.getAllItems();
        List<User> users = userService.getAllUsers();
        List<TakeRecord> records = itemService.getAllTakeRecords(); // 假设 Record 是正确的类名


        model.addAttribute("items", items);
        model.addAttribute("users", users);
        model.addAttribute("takeRecords", records);

        // 按月统计
        Map<String, Integer> monthlyStats = new HashMap<>();
        for (TakeRecord r : records) {
            String key = r.getCreatedAt().getYear() + "-" + (r.getCreatedAt().getMonthValue());
            monthlyStats.put(key, monthlyStats.getOrDefault(key, 0) + 1);
        }

        model.addAttribute("monthlyStats", monthlyStats);
        return "admin-dashboard";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit-item")
    public String editItemForm(@RequestParam Long id, Model model) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return "error"; // 返回错误页面
        }
        model.addAttribute("item", item);
        return "admin-edit-item";
    }


    @PostMapping("/update-item")
    public String updateItem(@ModelAttribute Item updatedItem) {
        Item existingItem = itemService.getItemById(updatedItem.getId());

        if (existingItem == null) {
            return "error";
        }

        // 同步 status 和 isStored
        existingItem.setName(updatedItem.getName());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setStatus(updatedItem.getStatus());
        existingItem.setStorageLocation(updatedItem.getStorageLocation());
        existingItem.setIsStored("STORED".equals(existingItem.getStatus())); // ✅ 关键：保持同步

        itemService.updateItem(existingItem);

        return "redirect:/admin/dashboard";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/records")
    public String viewAllRecords(Model model) {
        try {
            // 获取所有取物记录
            List<TakeRecord> takeRecords = itemService.getAllTakeRecords();
            model.addAttribute("takeRecords", takeRecords);
            
            // 获取总记录数
            int totalRecords = takeRecords.size();
            model.addAttribute("totalRecords", totalRecords);
            
            // 按月统计取物记录，直接基于record表的数据
            Map<String, Long> monthlyStats = new HashMap<>();
            for (TakeRecord record : takeRecords) {
                // 确保createdAt非空
                if (record.getCreatedAt() != null) {
                    // 格式化为"年-月"形式
                    String yearMonth = String.format("%d-%02d", 
                        record.getCreatedAt().getYear(), 
                        record.getCreatedAt().getMonthValue());
                    
                    // 按月累加记录数
                    monthlyStats.put(yearMonth, monthlyStats.getOrDefault(yearMonth, 0L) + 1);
                }
            }
            
            // 将Map转换为JSON字符串
            Gson gson = new Gson();
            String monthlyStatsJson = gson.toJson(monthlyStats);
            model.addAttribute("monthlyStats", monthlyStatsJson);
            
            // 统计物品类别分布
            Map<String, Long> categoryStats = new HashMap<>();
            for (TakeRecord record : takeRecords) {
                try {
                    // 通过物品ID获取物品类别
                    Item item = itemService.getItemById(record.getItemId());
                    String category = (item != null && item.getCategory() != null) 
                        ? item.getCategory() : "未知";
                    
                    // 按类别累加记录数
                    categoryStats.put(category, categoryStats.getOrDefault(category, 0L) + 1);
                } catch (Exception e) {
                    // 忽略单个物品的错误
                    categoryStats.put("未知", categoryStats.getOrDefault("未知", 0L) + 1);
                }
            }
            
            // 将类别统计转换为JSON字符串
            String categoryStatsJson = gson.toJson(categoryStats);
            model.addAttribute("recordsByCategory", categoryStatsJson);
            
            return "admin-records";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "处理数据时发生错误: " + e.getMessage());
            return "error";
        }
    }




}