package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.dto.*;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.StatusEnum;
import com.vegi.vegilabback.repository.*;
import com.vegi.vegilabback.service.RecipeService;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<SimpleRecipeDto> getRecipes(List<Recipe> recipeList) {
        List<SimpleRecipeDto> recipes = new ArrayList<>();
        for (var recipe: recipeList) {
            SimpleRecipeDto simpleRecipeDto = mapper.map(recipe, SimpleRecipeDto.class);
            recipes.add(simpleRecipeDto);
        }
        return recipes;
    }

    @Override
    public ReadRecipeDto getRecipe(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if(recipe.isPresent()){
            ReadRecipeDto readRecipeDto = mapper.map(recipe, ReadRecipeDto.class);
            return readRecipeDto;
        }
        return null;
    }

    public void addOrCreateCatToRec(Recipe recipe) {
        // Copie la liste de recette
        Set<Category> categories = recipe.getCategories();

        // Réinitialise la liste des recettes à vide
        recipe.setCategories(new HashSet<>());
        for (var cat : categories) {
            Long catId = cat.getId();

            // Si la cétgorie existe, on la l'ajoute
            if (catId != null) {
                Optional<Category> _cat = categoryRepository.findById(catId);
                recipe.addCategory(_cat.get());
            }
            // Sinon on crée une nouvelle recette avec is Added à false
            else {
                cat.setAdded(false);
                categoryRepository.save(cat);
                recipe.addCategory(cat);
            }
        }
    }

    @Override
    public Recipe createRecipe(CreateRecipeDto createRecipeDto, Long id) {
        var user =  userRepository.getReferenceById(id);
        var recipe = mapper.map(createRecipeDto, Recipe.class);
        recipe.setStatus(StatusEnum.EN_ATTENTE);
        recipe.setUser(user);
        addOrCreateCatToRec(recipe);
        this.recipeRepository.save(recipe);
        return recipe;
    }

    @Override
    public void deleteAllRecipesFromUser(@NonNull Long id) {
        List<Recipe> recipes = recipeRepository.findAllByUser(id);

        for (var recipe: recipes) {
            if(recipe !=null){
            recipeRepository.deleteAll(recipes);
            }
        }
    }

    @Override
    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.getById(id);
        recipeRepository.delete(recipe);
    }

    @Override
    public void addRecipeToFavorite(RecipeWithFavoritesDto recipeWithFavoritesDto, Long userId) {
        User user = userRepository.getReferenceById(userId);
        var recipe = recipeRepository.getById(recipeWithFavoritesDto.getId());
        user.addRecipe(recipe);
        this.userRepository.save(user);
        this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateStatus(Long recId, StatusEnum status) {
        var recipe = recipeRepository.findById(recId);
        recipe.get().setStatus(status);
        return recipe.get();
    }

    @Override
    public List<Recipe> findRecipeById(List<Long> recipes) {
        for (var r:recipes) {
            System.out.println("rec ----->"+ r);
        }
        return null;
    }


    @Override
    public Recipe updateRecipe(UpdateRecipeDto updateRecipeDto, Long recId, Long userId) {
        var recipe = recipeRepository.getById(recId);
        var user = recipe.getUser();
        if(recipe.getUser().getId() == userId){
            recipe = mapper.map(updateRecipeDto, Recipe.class);
            recipe.setUser(user);
            recipe.setStatus(StatusEnum.EN_ATTENTE);
            addOrCreateCatToRec(recipe);
            recipeRepository.save(recipe);
            return recipe;
        }
        return null;
    }


}
