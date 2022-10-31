package com.vegi.vegilabback;

import com.vegi.vegilabback.exception.exceptions.BusinessException;
import com.vegi.vegilabback.exception.exceptions.ResourceNotFoundException;
import com.vegi.vegilabback.exception.exceptions.TechnicalException;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.repository.IngredientRepository;
import com.vegi.vegilabback.service.IngredientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class IngredientServiceTest {
    @Autowired
    IngredientService ingredientService;

    @MockBean
    IngredientRepository ingredientRepository;

    Ingredient fakeIngredient = new Ingredient();

    public Ingredient setFakeIngredient(){
        fakeIngredient = Ingredient.builder().id(1L).label("tomate(s)").isAdded(false).build();
        return fakeIngredient;
    }

    @BeforeEach
    public void set_up(){
        setFakeIngredient();
    }

    @Test
    public void should_create_ingredient(){
        Ingredient newIngredient = ingredientService.createIngredient(fakeIngredient);

        Mockito.verify(ingredientRepository, Mockito.times(1)).save(newIngredient);
        Assertions.assertInstanceOf(Ingredient.class, newIngredient);
        Assertions.assertEquals(fakeIngredient, newIngredient);
        Assertions.assertEquals(fakeIngredient.getId(), newIngredient.getId());
        Assertions.assertEquals(fakeIngredient.getLabel(), newIngredient.getLabel());
        Assertions.assertEquals(fakeIngredient.isAdded(), newIngredient.isAdded());
    }

    @Test
    public void create_ingredient_return_bussiness_error_if_ingredient_null() throws Exception {
        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.createIngredient(null));
        Assertions.assertEquals(BusinessException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("l'ingrédient ne peut être null"));
    }

    @Test
    public void create_ingredient_return_technical_error() throws Exception {
        Mockito.when(ingredientRepository.save(any())).thenThrow(new DataIntegrityViolationException("toto"));

        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.createIngredient(fakeIngredient));
        Assertions.assertEquals(TechnicalException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("toto"));
    }

    @Test
    public void should_return_ingredient_by_id() throws Exception {
        Mockito.when(ingredientRepository.findById(1L)).thenReturn(Optional.of(fakeIngredient));

        Ingredient ingredient = ingredientService.getById(fakeIngredient.getId());
        Mockito.verify(ingredientRepository, Mockito.times(1)).findById(1L);
        Assertions.assertEquals(fakeIngredient, ingredient);
    }

    @Test
    public void get_by_id_return_resource_not_found() {
        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.getById(1L));
        Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("Aucun ingrédient avec l'id 1"));
    }

    @Test
    public void get_by_id_return_technical_error() throws Exception {
        Mockito.when(ingredientRepository.findById(1L)).thenThrow(new DataIntegrityViolationException("toto"));

        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.getById(1L));
        Assertions.assertEquals(TechnicalException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("toto"));
    }

    @Test
    public void should_return_all_ingredients() throws Exception {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(fakeIngredient);
        ingredientList.add(Ingredient.builder().isAdded(false).label("patate(s)").id(2L).build());
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> ingredients= ingredientService.getAllIngredients();

        Mockito.verify(ingredientRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(ingredients, "ingredients mustn't be empty");
        Assertions.assertEquals(2, ingredients.size(), "size not corresponding");
        Assertions.assertEquals(fakeIngredient, ingredients.get(0));
        Assertions.assertEquals(2, ingredients.get(1).getId());
    }

    @Test
    public void get_all_ingredients_return_bussiness_error() throws Exception {
        List<Ingredient> ingredientList = new ArrayList<>();
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredientList);

        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.getAllIngredients());
        Assertions.assertEquals(BusinessException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("Aucun ingrédient"));
    }

    @Test
    public void get_ingredients_for_user() throws Exception {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(fakeIngredient);
        ingredientList.add(Ingredient.builder().isAdded(true).label("patate(s)").id(2L).build());
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredientList);

        List<Ingredient> ingredients= ingredientService.getIngredientsForUser();

        Mockito.verify(ingredientRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(ingredients, "ingredients mustn't be empty");
        Assertions.assertEquals(1, ingredients.size(), "size not corresponding");
        Assertions.assertEquals(2L, ingredients.get(0).getId());
        Assertions.assertTrue(ingredients.get(0).isAdded());
        Assertions.assertEquals("patate(s)", ingredients.get(0).getLabel());
    }

    @Test
    public void get_ingredients_for_user_return_bussiness_error() throws Exception {
        List<Ingredient> ingredientList = new ArrayList<>();
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredientList);

        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.getIngredientsForUser());
        Assertions.assertEquals(BusinessException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("Aucun ingrédient"));
    }

    @Test
    public void should_update_ingredient(){
        Ingredient changedIngredient = Ingredient.builder().id(1L).isAdded(true).label("tomate(s) grappe").build();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(changedIngredient);
        Ingredient updatedIngredient = ingredientService.updateIngredient(changedIngredient);

        Mockito.verify(ingredientRepository, Mockito.times(1)).save(changedIngredient);
        Assertions.assertInstanceOf(Ingredient.class, updatedIngredient);
        Assertions.assertNotEquals(fakeIngredient, updatedIngredient);
        Assertions.assertEquals(1l, updatedIngredient.getId());
        Assertions.assertEquals("tomate(s) grappe", updatedIngredient.getLabel());
        Assertions.assertTrue(updatedIngredient.isAdded());
    }

    @Test
    public void update_ingredient_return_bussiness_error_if_ingredient_null() throws Exception {
        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.updateIngredient(null));
        Assertions.assertEquals(BusinessException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("l'ingrédient ne peut être null"));
    }

    @Test
    public void update_ingredient_return_technical_error() throws Exception {
        Mockito.when(ingredientRepository.save(any())).thenThrow(new DataIntegrityViolationException("toto"));

        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.updateIngredient(fakeIngredient));
        Assertions.assertEquals(TechnicalException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("toto"));
    }

    @Test
    public void should_delete_ingredient() throws Exception {
        Mockito.when(ingredientRepository.findById(1L)).thenReturn(Optional.of(fakeIngredient));

        ingredientService.deleteIngredient(fakeIngredient.getId());

        Mockito.verify(ingredientRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(ingredientRepository, Mockito.times(1)).delete(fakeIngredient);
    }

    @Test
    public void delete_ingredient_bussiness_error_if_ingredient_null() throws Exception {
        Exception e = Assertions.assertThrows(Exception.class, () -> ingredientService.deleteIngredient(1L));
        Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
        Assertions.assertTrue(e.getMessage().contains("Aucun ingrédient avec l'id 1"));
    }

}
