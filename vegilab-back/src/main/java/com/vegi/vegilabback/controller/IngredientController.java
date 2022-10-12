package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.service.IngredientService;
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
public class IngredientController {
    @Autowired
    IngredientService ingredientService;
    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredientsForUser() {
        List<Ingredient> ingredients = ingredientService.getIngredientsForUser();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/ingredient/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<List<Ingredient>> getAllIngredients(AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)){
            List<Ingredient> ingredients = ingredientService.getAllIngredients();
            return ResponseEntity.ok(ingredients);
        } else {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

/*    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Ingredient> createIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        Ingredient updatedIngredient = ingredientService.updateIngredient(ingredient);
        if(updatedIngredient.getId() != null){
            return new ResponseEntity<>(newIngredient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }*/
}
