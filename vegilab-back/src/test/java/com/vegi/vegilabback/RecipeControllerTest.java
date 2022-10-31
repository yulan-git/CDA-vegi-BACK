package com.vegi.vegilabback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegi.vegilabback.controller.RecipeController;
import com.vegi.vegilabback.dto.CreateRecipeDto;
import com.vegi.vegilabback.dto.ReadRecipeDto;
import com.vegi.vegilabback.dto.SimpleRecipeDto;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.*;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.security.jwt.AuthTokenFilter;
import com.vegi.vegilabback.security.jwt.JwtUtils;
import com.vegi.vegilabback.service.RecipeService;
import com.vegi.vegilabback.service.UserService;
import io.jsonwebtoken.Jwt;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.*;
import javax.servlet.Filter;
import javax.servlet.http.Cookie;

@WebMvcTest(value = RecipeController.class, includeFilters = {
        // to include JwtUtil in spring context
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtils.class)})
public class RecipeControllerTest {

    Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @MockBean
    private RecipeService recipeService;

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtUtils jwtUtil;

    @MockBean
    private AuthTokenFilter filter;

    User fakeUser = new User();
    User fakeAdmin = new User();
    Recipe fakeRecipe = new Recipe();

    public User setFakeUser(){
        Role roleUser = new Role(1, RoleEnum.USER);
        fakeUser = User.builder().id(1L).username("testUser").email("user@test.com").password("testUser0?").role(roleUser).build();
        return fakeUser;
    }

    public User setFakeAdmin(){
        Role roleAdmin = new Role(2, RoleEnum.ADMIN);
        fakeAdmin = User.builder().id(2L).username("testAdmin").email("admin@test.com").password("testAdmin0?").role(roleAdmin).build();
        return fakeUser;
    }

    public Recipe setFakeRecipe() {
        List<String> steps = new ArrayList<>();
        steps.add("Laver et éplucher les pommes");
        steps.add("Couper les pommes en tranches épaisses");

        Set<Category> categories = new HashSet<>();
        categories.add(Category.builder().id(1L).isAdded(true).label("vegan").build());

        Ingredient firstIngr = Ingredient.builder().id(1L).isAdded(true).label("tomate(s)").build();

        List<IngredientList> ingredients = new ArrayList<>();
        ingredients.add(IngredientList.builder().ingredient(firstIngr).quantity(2).unit(null).build());

        fakeRecipe.setName("tarte aux pommes");
        fakeRecipe.setDescription("tarte tatin aux pommes entièrement faite maison");
        fakeRecipe.setUrlImage(null);
        fakeRecipe.setCookTime(10);
        fakeRecipe.setPrepareTime(10);
        fakeRecipe.setDifficulty(DifficultyEnum.FACILE);
        fakeRecipe.setCost(CostEnum.BON_MARCHE);
        fakeRecipe.setSteps(steps);
        fakeRecipe.setPublishDate(null);
        fakeRecipe.setUser(fakeUser);
        fakeRecipe.setCategories(categories);
        fakeRecipe.setIngredients(ingredients);
        return fakeRecipe;
    }

    public static RequestPostProcessor testUser() {
        return user("testUser").roles("USER").password("testUser0?");
    }

    @BeforeEach
    public void setup() {
        setFakeRecipe();
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/").with(user("testUser").roles("USER")))
                .apply(springSecurity())
                .build();
    }

    @Test
    void should_return_recipe() throws Exception {
        ReadRecipeDto recipe = objectMapper.convertValue(fakeRecipe, ReadRecipeDto.class);
        when(recipeService.getRecipe(1L)).thenReturn(recipe);
        mockMvc.perform(get("/api/recipes/{id}", 1L)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipe.getId()))
                .andExpect(jsonPath("$.name").value(recipe.getName()))
                .andExpect(jsonPath("$.description").value(recipe.getDescription()))
                .andExpect(jsonPath("$.ingredients[0].quantity").value(2))
                .andExpect(jsonPath("$.ingredients[0].ingredient.label").value("tomate(s)"))
                .andExpect(jsonPath("$.categories[0].label").value("vegan"))
                .andExpect(jsonPath("$.user.username").value(recipe.getUser().getUsername()))
                .andExpect(status().isOk())
                .andDo(print());
    }



/*    @Test
    @WithMockUser(authorities = "USER")
    public void should_create_product() throws Exception {
        Role userRole = Role.builder().id(1).name(RoleEnum.USER).build();
        User user = User.builder().username("userTest").id(1L).email("toto@mail.com").password("Usertest0?").role(userRole).build();

        ResponseCookie cookies = jwtUtil.generateJwtCookie(user);
        String token = jwtUtil.generateTokenFromUsername(user.getUsername());

        Cookie cookie = new Cookie("vegilab", token);
        System.out.println(cookies);
        this.mockMvc
                .perform(
                        post("/api/recipe")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(fakeRecipe))
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }*/

}
