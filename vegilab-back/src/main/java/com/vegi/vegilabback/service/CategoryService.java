package com.vegi.vegilabback.service;

import com.vegi.vegilabback.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();
    Category createCategory(Category category);
}
