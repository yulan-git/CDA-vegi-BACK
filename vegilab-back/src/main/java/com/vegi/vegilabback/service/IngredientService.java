package com.vegi.vegilabback.service;

import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.model.IngredientList;

import java.util.List;

public interface IngredientService {
    List<Ingredient> getIngredientsForUser();
    List<Ingredient> getAllIngredients();
    Ingredient updateIngredient(Ingredient ingredient);
    public Ingredient createIngredient(Ingredient ingredient);
}
