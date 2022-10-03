package com.vegi.vegilabback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class CreateRecipeDto {
    private String name;
    private String description;
    private String urlImage;
    private int cookTime;
    private int prepareTime;
    private Date publishDate;
    private int nbPerson;
    private DifficultyEnum difficulty;
    private CostEnum cost;
    List<String> steps = new ArrayList<>();
    Set<Category> categories;
}
