package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public Iterable<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }
}
