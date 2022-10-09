package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.exception.ResourceNotFoundException;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.CategoryService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Repository
@Getter
@Setter
@RequestMapping("api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @GetMapping("/category")
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

    @PostMapping("/rec/{recId}/categories")
    public ResponseEntity<Category> addCategory(@PathVariable(value = "recId") Long recId, @RequestBody Category catRequest) {
        Category category = recipeRepository.findById(recId).map(rec -> {
            long catId = catRequest.getId();

            // tag is existed
            if (catId != 0L) {
                Category _cat = categoryRepository.findById(catId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + catId));
                rec.addCategory(_cat);
                recipeRepository.save(rec);
                return _cat;
            }

            // add and create new Tag
            rec.addCategory(catRequest);
            return categoryRepository.save(catRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + recId));

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
}
