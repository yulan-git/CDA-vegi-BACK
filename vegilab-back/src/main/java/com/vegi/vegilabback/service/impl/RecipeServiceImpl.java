package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.dto.ReadRecipeDto;
import com.vegi.vegilabback.dto.CreateRecipeDto;
import com.vegi.vegilabback.dto.SimpleRecipeDto;
import com.vegi.vegilabback.model.CategoriesList;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.enums.StatusEnum;
import com.vegi.vegilabback.repository.CategoriesListRepository;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoriesListRepository categoriesListRepository;
    @Autowired
    CategoryRepository categoryRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<ReadRecipeDto> getRecipes() {
        List<ReadRecipeDto> recipes = new ArrayList<>();
        Iterable<Recipe> recipeList = recipeRepository.findAll();
        for (var recipe: recipeList) {
            ReadRecipeDto readRecipeDto = addCategoriesToRecipe(recipe);
            recipes.add(readRecipeDto);
        }
        return recipes;
    }

    @Override
    public ReadRecipeDto getRecipe(Long id) {
        var recipe = recipeRepository.getById(id);
        if(recipe != null){
            ReadRecipeDto readRecipeDto = addCategoriesToRecipe(recipe);
            return readRecipeDto;
        }
        return null;
    }

    private ReadRecipeDto addCategoriesToRecipe(Recipe recipe) {
        Set<Category> categories = new HashSet<>();
        List<CategoriesList> categoriesList = categoriesListRepository.getCategoriesListByRecipe(recipe);
        for (var catL : categoriesList) {
            var cat = categoryRepository.getById(catL.getCategory().getId());
            categories.add(cat);
        }
        ReadRecipeDto readRecipeDto = mapper.map(recipe, ReadRecipeDto.class);
        readRecipeDto.setCategories(categories);
        return readRecipeDto;
    }

    @Override
    public Recipe createRecipe(CreateRecipeDto createRecipeDto) {
        var user =  userRepository.getReferenceById(1L);
        var recipe = mapper.map(createRecipeDto, Recipe.class);
        recipe.setName(createRecipeDto.getName());
        recipe.setStatus(StatusEnum.EN_ATTENTE);
        recipe.setUser(user);
        this.recipeRepository.save(recipe);
        Set<CategoriesList> categoriesList = new HashSet<>();
        addCategories(createRecipeDto, recipe, categoriesList);
        return recipe;
    }


    private void addCategories(CreateRecipeDto createRecipeDto, Recipe recipe, Set<CategoriesList> categoriesList) {
        for (var cat: createRecipeDto.getCategories()
        ) {
            var categories = new CategoriesList();
            categories.setCategory(cat);
            categories.setRecipe(recipe);
            categoriesListRepository.save(categories);
            categoriesList.add(categories);
        }
    }

    @Override
    public List<ReadRecipeDto> getRecipesByCategory(String category) {
        List<ReadRecipeDto> recipes = getRecipes();
        List<ReadRecipeDto> recipesByCat = new ArrayList<>();
        for (var recipe  : recipes) {
            Set<Category> categories = recipe.getCategories();
            for (var cat: categories){
                if (cat.getLabel().equals(category)){
                    recipesByCat.add(recipe);
                }
            }
        }
        return recipesByCat;
    }



}
