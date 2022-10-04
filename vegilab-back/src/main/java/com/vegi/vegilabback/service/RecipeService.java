package com.vegi.vegilabback.service;

import com.vegi.vegilabback.dto.ReadRecipeDto;
import com.vegi.vegilabback.dto.CreateRecipeDto;
import com.vegi.vegilabback.dto.SimpleRecipeDto;
import com.vegi.vegilabback.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    List<ReadRecipeDto>getRecipes();
    ReadRecipeDto getRecipe(Long id);
    Recipe createRecipe(CreateRecipeDto recipe);
    List<ReadRecipeDto> getRecipesByCategory(String category);
}
