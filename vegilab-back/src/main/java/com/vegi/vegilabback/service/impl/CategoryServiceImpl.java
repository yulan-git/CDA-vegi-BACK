package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.exception.exceptions.ResourceNotFoundException;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;
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
    public List<Category> getCategoriesForUser() {
        List<Category> categories = categoryRepository.findAll();
        categories.removeIf(category -> {
            if(!category.isAdded()){
                return true;
            }
            return false;
        });
        return categories;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    @Override
    public Category getById(Long id) {
        var category = categoryRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found ingredient with Id"+ id));
        return category;
    }
}
