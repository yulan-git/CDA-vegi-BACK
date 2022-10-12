package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.exception.ResourceNotFoundException;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.CategoryService;
import com.vegi.vegilabback.validation.AuthentificationValidation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategoriesForUser() {
        List<Category> categories = categoryService.getCategoriesForUser();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<List<Category>> getAllCategories(AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)){
            List<Category> ingredients = categoryService.getAllCategories();
            return ResponseEntity.ok(ingredients);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
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
