/*
package com.vegi.vegilabback;

import com.vegi.vegilabback.dto.CreateRecipeDto;
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
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class RecipeServiceTest {

    Recipe fakeRecipe = new Recipe();

    public User setFakeUser(){
        Role role = new Role(RoleEnum.USER);
        role.setName(RoleEnum.USER);
        role.setId(1);

        User fakeUser = new User(1L, "YulanUser", "flo@mail.com", "Floflo56?", null, role, null);
        return fakeUser;
    }

    public Recipe setFakeRecipe() throws ParseException {

        Set<Category> categorySet = new HashSet<>();
        Category cat1 = new Category(1L, "Entrée", true, null);
        categorySet.add(cat1);

        String sDate ="20-09-2022";
        Date date=new SimpleDateFormat("dd-MM-yyyy").parse(sDate);

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("tarte aux pommes");
        recipe.setDescription("tarte tatin aux pommes entièrement faite maison");
        recipe.setCookTime(20);
        recipe.setPrepareTime(20);
        recipe.setPublishDate(date);
        recipe.setUrlImage(null);
        recipe.setNbPerson(2);
        recipe.setCost(CostEnum.BON_MARCHE);
        recipe.setDifficulty(DifficultyEnum.FACILE);
        recipe.setCategories(categorySet);
        recipe.setUser(setFakeUser());
        return fakeRecipe;
    }

    public RecipeService getRecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        return new RecipeServiceImpl(categoryRepository, recipeRepository, userRepository);
    }

    @BeforeEach
    public void set_up() throws ParseException {
        setFakeUser();
        setFakeRecipe();
    }

    @Test
    public void createRecipe() throws Exception {

        List<String> steps = new ArrayList<>();
        String step1 = "Laver et éplucher les pommes";
        String step2 = "Couper les pommes en tranches épaisses";
        steps.add(step1); steps.add(step2);
        String sDate ="20-09-2022";
        Date date=new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
        Set<Category> categories = new HashSet<>();

        Role role = Role.builder().name(RoleEnum.USER).build();
        Category cat = Category.builder().id(1L).isAdded(true).label("Entrée").recipes(null).build();
        categories.add(cat);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RecipeRepository recipeRepository = Mockito.mock(RecipeRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);

        User user = User.builder().id(1L).username("YulanUser").email("flo@mail.com").password("Floflo56?").resetPasswordToken(null).role(role).build();
        System.out.println( "USER *************"+  user.getId());
        CreateRecipeDto recipe = CreateRecipeDto.builder()
                .cookTime(20)
                .prepareTime(20)
                .cost(CostEnum.BON_MARCHE)
                .description("tarte tatin aux pommes entièrement faite maison")
                .difficulty(DifficultyEnum.FACILE)
                .nbPerson(2)
                .urlImage(null)
                .publishDate(date)
                .steps(steps)
                .categories(categories).build();

        //Mockito.when(userRepository.findById(1L)).thenReturn(user);
        //Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(recipeRepository.save(any())).thenReturn(Recipe.builder().id(1L).categories(Set.of(cat)).user(user).build());

        Recipe newRecipe = getRecipeService(recipeRepository, categoryRepository, userRepository).createRecipe(recipe, user.getId());
        Assertions.assertEquals(fakeRecipe, newRecipe);
        //Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(1L);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(any());
        Mockito.verify(recipeRepository, Mockito.times(1)).save(any());
    }
}
*/
