package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.exception.exceptions.BusinessException;
import com.vegi.vegilabback.exception.exceptions.IngredientException;
import com.vegi.vegilabback.exception.exceptions.ResourceNotFoundException;
import com.vegi.vegilabback.exception.exceptions.TechnicalException;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.repository.IngredientRepository;
import com.vegi.vegilabback.service.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getIngredientsForUser() {
        try {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        if(ingredients.isEmpty()){
            throw new BusinessException("Aucun ingrédient");
        }
        ingredients.removeIf(ingredient -> {
            return !ingredient.isAdded();
        });
        return ingredients;
        } catch (BusinessException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        try{
            List<Ingredient> ingredients = ingredientRepository.findAll();
            if(ingredients.isEmpty()){
                throw new BusinessException("Aucun ingrédient");
            }
            return ingredients;
        } catch (BusinessException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        try{
            if(ingredient == null){
                throw new BusinessException("l'ingrédient ne peut être null");
            }
            Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return updatedIngredient;
        } catch (BusinessException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        try{
            if(ingredient == null){
                throw new BusinessException("l'ingrédient ne peut être null");
            }
            ingredientRepository.save(ingredient);
            return ingredient;
        } catch (BusinessException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void deleteIngredient(Long id){
        try {
            var ingredient = ingredientRepository
                    .findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Aucun ingrédient avec l'id "+ id));
            ingredientRepository.delete(ingredient);
        } catch (ResourceNotFoundException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void deleteIngredientForUser(Long id){
        try {
            var ingredient = ingredientRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Aucun ingrédient avec l'id "+ id));
            if(!ingredient.isAdded()){
                ingredientRepository.delete(ingredient);
            }
        } catch (ResourceNotFoundException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void deleteIngredients(List<Ingredient> ingredients){
        try {
            if(ingredients.isEmpty()){
                throw new BusinessException("Aucun ingrédient");
            }
            ingredientRepository.deleteAll(ingredients);
        } catch (ResourceNotFoundException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Ingredient getById(Long id) {
        try{
            var ingredient = ingredientRepository
                    .findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Aucun ingrédient avec l'id "+ id));
            return ingredient;
        } catch (ResourceNotFoundException e){
            IngredientServiceImpl.log.warn(e.getMessage());
            throw e;
        } catch (Exception e){
            throw new TechnicalException(e.getMessage(), e.getCause());
        }
    }
}
