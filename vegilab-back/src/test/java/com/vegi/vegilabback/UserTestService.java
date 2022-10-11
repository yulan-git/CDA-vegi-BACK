package com.vegi.vegilabback;

import com.vegi.vegilabback.dto.*;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.Role;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.RoleEnum;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.service.RecipeService;
import com.vegi.vegilabback.service.impl.RecipeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserTestService {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RecipeService recipeService;

    public RecipeService getRecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        return new RecipeServiceImpl(categoryRepository, recipeRepository, userRepository);
    }

    @Test
    @DisplayName("Should Retrieve Recipe by Id")
    public void shouldFindRecipeById() throws ParseException {
        Set<Category> categorySet = new HashSet<>();
        Category cat1 = new Category();
        cat1.setAdded(true);
        cat1.setLabel("entrée");
        cat1.setRecipes(null);
        cat1.setId(2L);
        categorySet.add(cat1);

        Role role = new Role(RoleEnum.USER);
        role.setName(RoleEnum.USER);
        role.setId(1);

        User fakeUser = new User();
        fakeUser.setId(2L);
        fakeUser.setPassword("Floflo56?");
        fakeUser.setUsername("YulanUser4");
        fakeUser.setLiked(null);
        fakeUser.setRole(role);
        fakeUser.setEmail("flora_564@live.fr");

        String sDate ="20-09-2022";
        Date date=new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("tarte aux pommes");
        recipe.setDescription("tarte tatin aux pommes entièrement faite maison");
        recipe.setCookTime(20);
        recipe.setPrepareTime(15);
        recipe.setPublishDate(date);
        recipe.setUrlImage(null);
        recipe.setNbPerson(2);
        recipe.setCost(CostEnum.BON_MARCHE);
        recipe.setDifficulty(DifficultyEnum.FACILE);
        recipe.setCategories(categorySet);
        recipe.setUser(fakeUser);

        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        ReadRecipeDto actualRecipe = recipeService.getRecipe(1L);
        System.out.println("actualRecipe"+ actualRecipe.getId());

        Assertions.assertThat(actualRecipe.getId()).isEqualTo(recipe.getId());
        Assertions.assertThat(actualRecipe.getName()).isEqualTo(recipe.getName());
    }
}
