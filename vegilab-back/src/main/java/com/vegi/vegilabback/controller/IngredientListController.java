package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.model.IngredientList;
import com.vegi.vegilabback.repository.IngredientListRepository;
import com.vegi.vegilabback.validation.AuthentificationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
public class IngredientListController {

    @Autowired
    private IngredientListRepository ingredientListRepository;

    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;

    @DeleteMapping("/ingredient-list/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR') or hasAuthority('USER')")
    public ResponseEntity<String> deleteIngredient(@PathVariable Long id, AuthentificationValidation authentificationValidation) {
        if(authentificationValidation.getTokenId()!=null) {
            ingredientListRepository.deleteById(id);
            return new ResponseEntity<>("Ingredient list deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
