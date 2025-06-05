package org.example.xmut2.lovegoods.controller;
import org.example.xmut2.lovegoods.model.TakeRecord;
import org.example.xmut2.lovegoods.model.Category;
import org.example.xmut2.lovegoods.model.Item;
import org.example.xmut2.lovegoods.model.User;
import org.example.xmut2.lovegoods.repository.UserRepository;
import org.example.xmut2.lovegoods.service.CategoryService;
import org.example.xmut2.lovegoods.service.ItemService;
import org.example.xmut2.lovegoods.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ItemService itemService; // ✅ 正确方式：只保留这一行

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("authorities", userDetails.getAuthorities());
        }
        return "student-dashboard";
    }

    @GetMapping("/store-item")
    public String storeItemForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "store-item";
    }

    @PostMapping("/store-item")
    public String storeItem(
            @RequestParam String name,
            @RequestParam String category,
            Model model,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cardId = userDetails.getUsername();

        User user = userRepository.findByCardId(cardId);
        if (user == null) {
            return "redirect:/login";
        }

        Item item = new Item();
        item.setName(name);
        item.setCategory(category);
        item.setStatus("STORED");
        item.setQrCode(UUID.randomUUID().toString());
        item.setUser(user);
        item.setIsStored(true);

        // 设置时间戳
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);

        Item storedItem = itemService.storeItem(item);

        // 生成二维码图片 Base64
        try {
            String qrCodeImage = QRCodeUtil.generateQRCodeImage(storedItem.getQrCode(), 300, 300);
            model.addAttribute("qrCodeImage", qrCodeImage);
            model.addAttribute("qrCode", storedItem.getQrCode());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "二维码生成失败");
        }

        return "store-item";
    }






    @GetMapping("/retrieve-item")
    public String retrieveItemForm(@RequestParam(required = false) String category, Model model) {
        List<Item> items = (category == null || category.isEmpty())
                ? itemService.getAllStoredItems()
                : itemService.getItemsByCategoryAndStored(category);

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        model.addAttribute("category", category); // 保持选中状态


        return "retrieve-item";
    }


    @PostMapping("/retrieve-item")
    public String retrieveItem(@RequestParam String qrCode, Model model) {
        Item item = itemService.retrieveItem(qrCode);

        if (item != null) {
            model.addAttribute("item", item);

            // 如果已取出，显示存储位置
            if (!item.isStored()) {
                model.addAttribute("storageLocation", item.getStorageLocation());
            }

            try {
                String qrCodeImage = QRCodeUtil.generateQRCodeImage(item.getQrCode(), 300, 300);
                model.addAttribute("qrCodeImage", qrCodeImage);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "二维码生成失败");
            }
        } else {
            model.addAttribute("error", "无效或不可取出的物品");
        }


        return "retrieve-item";
    }



    @PostMapping("/do-retrieve")
    public String doRetrieveItem(@RequestParam String qrCode, Model model, Authentication authentication) {
        Item item = itemService.retrieveItem(qrCode);

        if (item == null || !item.isStored()) {
            model.addAttribute("error", "无效或不可取出的物品");
            return "retrieve-item";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String cardId = userDetails.getUsername();

        User user = userRepository.findByCardId(cardId);
        if (user == null) {
            return "redirect:/login";
        }

        // 判断是否本月已取过
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<TakeRecord> takeRecords = itemService.findRecordsByUserIdAndType(user.getId(), "TAKE");
        boolean hasTakenThisMonth = takeRecords.stream()
                .anyMatch(takeRecord -> takeRecord.getCreatedAt().isAfter(startOfMonth));

        if (hasTakenThisMonth) {
            model.addAttribute("error", "您本月已取过一件物品，无法再次领取");
            return "retrieve-item";
        }

        // 更新物品状态为已取
        item.setIsStored(false);
        item.setStatus("TAKEN");
        itemService.updateItem(item);

        // 记录取物行为
        itemService.saveTakeRecord(item, user);

        model.addAttribute("item", item);
        model.addAttribute("storageLocation", item.getStorageLocation());

        return "retrieve-item";
    }


    @GetMapping("/view-qr-code")
    public String viewQRCode(
            @RequestParam String qrCode,
            @RequestParam(required = false) String currentCategory, // 改用 currentCategory 避免冲突
            Model model) {

        Item item = itemService.retrieveItem(qrCode);
        if (item == null) {
            model.addAttribute("error", "该二维码不存在");
            return "retrieve-item";
        }

        try {
            String qrCodeImage = QRCodeUtil.generateQRCodeImage(item.getQrCode(), 300, 300);
            model.addAttribute("qrCodeImage", qrCodeImage);
            model.addAttribute("item", item);
            model.addAttribute("currentCategory", currentCategory); // 保持一致
        } catch (Exception e) {
            model.addAttribute("error", "生成二维码失败：" + e.getMessage());
            e.printStackTrace();
        }

        return "retrieve-item";
    }











}
