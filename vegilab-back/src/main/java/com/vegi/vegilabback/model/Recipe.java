package com.vegi.vegilabback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String urlImage;

    @NotBlank
    private int cookTime;

    @NotBlank
    private int prepareTime;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date publishDate;

    @NotBlank
    private int nbPerson;

    @NotBlank
    private StatusEnum status;

    @NotBlank
    private DifficultyEnum difficulty;

    @NotBlank
    private CostEnum cost;

    @ElementCollection
    List<String> steps = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "favoritesRecipes")
    @JsonIgnore
    Set<User> likes;

    @OneToMany(mappedBy = "recipe")
    Set<Preparation> preparations;

    @OneToMany(mappedBy = "recipe")
    Set<Planning> plannings;

    @ManyToMany
    @JoinTable(
            name = "categories_list",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categories;
}
