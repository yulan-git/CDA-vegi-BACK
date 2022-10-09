package com.vegi.vegilabback.service;

import com.vegi.vegilabback.dto.*;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.model.enums.StatusEnum;

import java.util.List;

public interface RecipeService {
    List<SimpleRecipeDto> getRecipes(List<Recipe> recipeDtos);
    ReadRecipeDto getRecipe(Long id);
    Recipe createRecipe(CreateRecipeDto recipe, Long id);
    Recipe updateRecipe(UpdateRecipeDto recipe, Long recId, Long userId);
    void deleteAllRecipesFromUser(Long id);
    void deleteRecipeById(Long id);
    void addRecipeToFavorite(RecipeWithFavoritesDto recipe, Long userId);
    Recipe updateStatus(Long recId, StatusEnum status);
}
