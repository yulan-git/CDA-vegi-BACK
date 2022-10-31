package com.vegi.vegilabback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.model.IngredientList;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ReadRecipeDto {
    private Long id;
    private String name;
    private String description;
    private String urlImage;
    private int cookTime;
    private int prepareTime;
    private LocalDate publishDate;
    private int nbPerson;
    private DifficultyEnum difficulty;
    private CostEnum cost;
    @JsonIncludeProperties("username")
    private UserDto user;
    List<String> steps = new ArrayList<>();
    Set<Category> categories;
    Set<IngredientList> ingredients;
    Set<UserDto> likes;
}
