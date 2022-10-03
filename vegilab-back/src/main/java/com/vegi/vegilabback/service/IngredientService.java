package com.vegi.vegilabback.service;

import com.vegi.vegilabback.model.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> getIngredients();
    public Ingredient createIngredient(Ingredient ingredient);
}
