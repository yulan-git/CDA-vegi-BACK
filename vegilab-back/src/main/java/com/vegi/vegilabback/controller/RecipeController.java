package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.dto.*;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.enums.StatusEnum;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.service.RecipeService;
import com.vegi.vegilabback.service.UserService;
import com.vegi.vegilabback.validation.AuthentificationValidation;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Getter
@Setter
@RequestMapping("api")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/recipes/{id}")
    public ResponseEntity<ReadRecipeDto> getRecipe(@PathVariable Long id) {
        ReadRecipeDto recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/recipe")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> addRecipe(@RequestBody CreateRecipeDto recRequest, AuthentificationValidation authentificationValidation) {
            var userId = authentificationValidation.getTokenId();
            if(userId != null) {
                Recipe recipe = recipeService.createRecipe(recRequest, userId);
                System.out.println("Recipe  3 ----->" + recipe);
                return new ResponseEntity<>("recette crée", HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

    }

    @PutMapping("/recipes/update/{recId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long recId, @RequestBody UpdateRecipeDto recRequest, AuthentificationValidation authentificationValidation){
            var userId = authentificationValidation.getTokenId();
            //var rec = mapper.map(recRequest, Recipe.class);
            if(userId != null) {
                Recipe recipe = recipeService.updateRecipe(recRequest, recId, userId);
                return new ResponseEntity<>(recipe, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
    }

    @GetMapping("/recipes/all")
    public ResponseEntity<Map<String, Object>> getRecipes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        try {
            List<Recipe> recipes = new ArrayList<Recipe>();
            Pageable paging = PageRequest.of(page, size);

            Page<Recipe> r;
            r = recipeRepository.findAll(paging);
            recipes = r.getContent();

            List<SimpleRecipeDto> recipeDtos= recipeService.getRecipes(recipes);

            Map<String, Object> response = new HashMap<>();
            response.put("recipes", recipeDtos);
            response.put("currentPage", r.getNumber());
            response.put("totalItems", r.getTotalElements());
            response.put("totalPages", r.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recipes/categories")
    public ResponseEntity<Map<String, Object>> getCat(@RequestParam(defaultValue = "0") String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        try {
            List<Recipe> recipes = new ArrayList<Recipe>();
            Pageable paging = PageRequest.of(page, size);

            Page<Recipe> r;
            r = recipeRepository.findByCategory(category, paging);
            recipes = r.getContent();

            List<SimpleRecipeDto> recipeDtos= recipeService.getRecipes(recipes);

            Map<String, Object> response = new HashMap<>();
            response.put("recipes", recipeDtos);
            response.put("currentPage", r.getNumber());
            response.put("totalItems", r.getTotalElements());
            response.put("totalPages", r.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/recipe/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity deleteRecipe(@PathVariable ("id") Long id, AuthentificationValidation authentificationValidation){
        Recipe recipe = recipeRepository.getById(id);
        if(authentificationValidation.getTokenUsername().equals(adminSecretName) || authentificationValidation.getTokenId().equals(recipe.getUser().getId())) {
            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/recipes/category")
    public ResponseEntity<List<SimpleRecipeDto>> getByCategory(@RequestParam Long id) {
        List<Recipe> recipeList = recipeRepository.findByCategories(id);
        List<SimpleRecipeDto> recipeByCategories = new ArrayList<>();
        for (var recipe: recipeList) {
            SimpleRecipeDto simpleRecipeDto = mapper.map(recipe, SimpleRecipeDto.class);
            recipeByCategories.add(simpleRecipeDto);
        }
        return ResponseEntity.ok(recipeByCategories);
    }

    @GetMapping("/recipes/famous")
    public ResponseEntity<List<SimpleRecipeDto>> getMostFamousRecipes() {
        List<Long> recipesId = recipeRepository.findByFamous();
        List<SimpleRecipeDto> famousRecipes = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>();
        for (var id : recipesId) {
            Optional<Recipe> rec = recipeRepository.findById(id);
            recipes.add(rec.get());
        }
        for (var r : recipes){
            SimpleRecipeDto simpleRecipeDto = mapper.map(r, SimpleRecipeDto.class);
            famousRecipes.add(simpleRecipeDto);
        }
        return ResponseEntity.ok(famousRecipes);
    }


    @GetMapping("/recipe/user/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR')") //faire liste de moderateur
    public ResponseEntity<Map<String, Object>> getByUser(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, AuthentificationValidation authentificationValidation) {
        try {
            if(authentificationValidation.getTokenId() == id ){
            List<Recipe> recipes = new ArrayList<Recipe>();
            Pageable paging = PageRequest.of(page, size);

            Page<Recipe> r;
            r = recipeRepository.findByUserId(id, paging);
            recipes = r.getContent();

            List<SimpleRecipeDto> recipeDtos= new ArrayList<>();

            for (var recipe: recipes) {
                SimpleRecipeDto simpleRecipeDto = mapper.map(recipe, SimpleRecipeDto.class);
                recipeDtos.add(simpleRecipeDto);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("recipes", recipeDtos);
            response.put("currentPage", r.getNumber());
            response.put("totalItems", r.getTotalElements());
            response.put("totalPages", r.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/recipe/favorites")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> addRecipeToFavorites(@RequestBody RecipeWithFavoritesDto recipeWithFavoritesDto, AuthentificationValidation authentificationValidation) {
       try{
            var userId = authentificationValidation.getTokenId();
            if(userId != null) {
                recipeService.addRecipeToFavorite(recipeWithFavoritesDto, userId);
                return new ResponseEntity<>("Recette ajoutée aux favoris", HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/recipe/status/{recId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MODERATOR')")
    public ResponseEntity<Recipe> changeRecipeStatus(@PathVariable Long recId, @RequestParam StatusEnum status, AuthentificationValidation authentificationValidation){
        try{
            if (authentificationValidation.getTokenUsername().equals(adminSecretName)){
                var updatedRecipe = recipeService.updateStatus(recId, status);
                return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
