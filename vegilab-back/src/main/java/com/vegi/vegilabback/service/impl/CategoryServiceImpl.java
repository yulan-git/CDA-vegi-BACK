package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        Boolean cat = categoryRepository.existsByLabel(category.getLabel());
        if(!cat == true){
            category.setAdded(false);
            categoryRepository.save(category);
        }
        return category;
    }
}
