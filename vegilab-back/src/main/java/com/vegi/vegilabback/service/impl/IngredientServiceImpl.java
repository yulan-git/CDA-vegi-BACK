package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.repository.IngredientRepository;
import com.vegi.vegilabback.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        Boolean ingr = ingredientRepository.existsByLabel(ingredient.getLabel());
            if(!ingr == true){
                ingredient.setAdded(false);
                ingredientRepository.save(ingredient);
            }
            return ingredient;
    }
}
