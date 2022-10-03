package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.service.IngredientService;
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
public class IngredientController {
    @Autowired
    IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/ingredient")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        Ingredient newIngredient = ingredientService.createIngredient(ingredient);
        if(newIngredient.getId() != null){
            return new ResponseEntity<>(newIngredient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
