package com.vegi.vegilabback.model;

import com.fasterxml.jackson.annotation.*;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Data
@Entity
@Table(name = "recipes")
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String description;

    private String urlImage;

    @NotNull
    private int cookTime;

    @NotNull
    private int prepareTime;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date publishDate;

    @NotNull
    private int nbPerson;

    @NotNull
    private StatusEnum status;

    @NotNull
    private DifficultyEnum difficulty;

    @NotNull
    private CostEnum cost;

    @ElementCollection
    List<String> steps = new ArrayList<>();

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(mappedBy = "favoritesRecipes")
    @JsonIgnore
    Set<User> likes;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    Set<Preparation> preparations;

    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    Set<Planning> plannings;


/*    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "recipe_categories",
            joinColumns = { @JoinColumn(name = "recipe_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private Set<Category> categories = new HashSet<>();*/

}
