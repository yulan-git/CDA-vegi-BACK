package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.dto.ReadRecipeDto;
import com.vegi.vegilabback.dto.CreateRecipeDto;
import com.vegi.vegilabback.dto.SimpleRecipeDto;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.RecipeService;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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

    ModelMapper mapper = new ModelMapper();

/*    @GetMapping("/recipes")
    public ResponseEntity<List<ReadRecipeDto>> getAllRecipes() {
        List<ReadRecipeDto> recipes = recipeService.getRecipes();
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }*/

    @GetMapping("/recipe/{id}")
    public ResponseEntity<ReadRecipeDto> getRecipe(@PathVariable Long id) {
        ReadRecipeDto recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/recipe")
    public ResponseEntity<List<ReadRecipeDto>> getRecipeByCategory(@RequestParam String category) {
        List<ReadRecipeDto> recipes;
        recipes = recipeService.getRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody CreateRecipeDto recipe) {
        Recipe newRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }


    @GetMapping("/recipes")
    public ResponseEntity<Map<String, Object>> getRecipes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        try {
            List<Recipe> recipes = new ArrayList<Recipe>();
            Pageable paging = PageRequest.of(page, size);

            Page<Recipe> r;
            r = recipeRepository.findAll(paging);
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
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recByCat")
    public ResponseEntity<List<SimpleRecipeDto>> getByCategory(@RequestParam Long id) {
        List<Recipe> recipeList = recipeRepository.findByCategories(id);
        List<SimpleRecipeDto> recipeByCategories = new ArrayList<>();
        for (var recipe: recipeList) {
            SimpleRecipeDto simpleRecipeDto = mapper.map(recipe, SimpleRecipeDto.class);
            recipeByCategories.add(simpleRecipeDto);
        }
        return ResponseEntity.ok(recipeByCategories);
    }

}
