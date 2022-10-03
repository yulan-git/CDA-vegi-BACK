package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.service.CategoryService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
@Getter
@Setter
@RequestMapping("api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.createCategory(category);
        if(newCategory.getId() != null){
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
