/*
package com.vegi.vegilabback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegi.vegilabback.controller.RecipeController;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.Role;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.RoleEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class RecipeControllerTest {
    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateRecipe() throws Exception {
        Set<Category> categorySet = new HashSet<>();
        Category cat1 = categoryRepository.getById(1L);
        Category cat2 = categoryRepository.getById(2L);
        categorySet.add(cat1);
        categorySet.add(cat2);

        Role role = new Role();
        role.setName(RoleEnum.USER);
        role.setId(1);

        User user = new User(1L, "YulanUser", "flo@mail.com", "Floflo56?", null, role, null);

        String sDate ="20-09-2022";
        Date date=new SimpleDateFormat("dd-MM-yyyy").parse(sDate);

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("tarte aux pommes");
        recipe.setDescription("tarte tatin aux pommes entièrement faite maison");
        recipe.setCookTime(20);
        recipe.setStatus(StatusEnum.EN_ATTENTE);
        recipe.setPublishDate(date);
        recipe.setUrlImage(null);
        recipe.setNbPerson(2);
        recipe.setCost(CostEnum.BON_MARCHE);
        recipe.setDifficulty(DifficultyEnum.FACILE);
        recipe.setLikes(null);
        recipe.setCategories(categorySet);
        recipe.setUser(user);

        mockMvc.perform(post("/api/recipe").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

}
*/
