package com.vegi.vegilabback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class CreateRecipeDto {
    private String name;
    private String description;
    private String urlImage;
    private int cookTime;
    private int prepareTime;
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date publishDate;
    private int nbPerson;
    private DifficultyEnum difficulty;
    private CostEnum cost;
    List<String> steps = new ArrayList<>();
    Set<Category> categories;
    Set<IngredientList> ingredients;
}
