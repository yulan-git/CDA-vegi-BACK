package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.model.enums.RoleEnum;
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

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
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

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getOneIngredient(@PathVariable Long id){
        if(id != null || id != 0L){
            Ingredient ingredient = ingredientService.getById(id);
            return new ResponseEntity<>(ingredient, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ingredient/new")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient, AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenId() != null){
            Ingredient newIngredient = ingredientService.createIngredient(ingredient);
            return new ResponseEntity<>(newIngredient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/ingredient/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient, AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)){
            Ingredient newIngredient = ingredientService.updateIngredient(ingredient);
            return new ResponseEntity<>(newIngredient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }



    @DeleteMapping("/ingredient/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('USER')")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long id, AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)) {
            ingredientService.deleteIngredient(id);
            return new ResponseEntity<>("Ingréient supprimé", HttpStatus.OK);
        }else if(authentificationValidation.getTokenId()!= null){
            ingredientService.deleteIngredientForUser(id);
            return new ResponseEntity<>("Ingrédient supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/ingredient/delete/list")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<String> deleteIngredients(@PathVariable Long id, @RequestBody List<Ingredient> ingredients, AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)){
            ingredientService.deleteIngredients(ingredients);
            return new ResponseEntity<>("Ingredients deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
