package com.vegi.vegilabback.service;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoriesForUser();
    List<Category> getAllCategories();
}
