package org.example.xmut2.lovegoods.service;

import org.example.xmut2.lovegoods.model.Category;
import org.example.xmut2.lovegoods.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
