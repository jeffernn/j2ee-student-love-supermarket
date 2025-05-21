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

        List<TakeRecord> takeRecords = itemService.getAllTakeRecords();

        // 按月统计次数
        Map<String, Integer> monthlyStats = takeRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> {
                            LocalDateTime createdAt = record.getCreatedAt();
                            return createdAt.getYear() + "-" + (createdAt.getMonthValue());
                        },
                        Collectors.summingInt(v -> 1)
                ));

        model.addAttribute("takeRecords", takeRecords);
        model.addAttribute("monthlyStats", new Gson().toJson(monthlyStats)); // ✅ 使用 Gson 转为 JSON 字符串

        return "admin-records";
    }




}