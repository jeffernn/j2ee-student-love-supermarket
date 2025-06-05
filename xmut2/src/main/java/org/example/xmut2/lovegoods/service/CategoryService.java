package org.example.xmut2.lovegoods.service;

import org.example.xmut2.lovegoods.model.Category;
import org.example.xmut2.lovegoods.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 物品类别服务类
 * 提供物品类别的相关业务操作
 */
@Service
public class CategoryService {

    /**
     * 物品类别数据访问对象
     * 用于操作物品类别数据
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 获取所有物品类别
     * @return 物品类别列表
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
