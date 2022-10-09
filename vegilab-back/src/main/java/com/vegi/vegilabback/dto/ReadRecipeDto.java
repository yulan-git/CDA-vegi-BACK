package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.Category;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    private Date publishDate;
    private int nbPerson;
    private DifficultyEnum difficulty;
    private CostEnum cost;
    private UserDto user;
    List<String> steps = new ArrayList<>();
    Set<Category> categories;
    Set<UserDto> likes;
}
