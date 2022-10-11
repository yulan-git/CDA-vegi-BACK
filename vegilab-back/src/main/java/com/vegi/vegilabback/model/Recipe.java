package com.vegi.vegilabback.model;

import com.fasterxml.jackson.annotation.*;
import com.vegi.vegilabback.model.enums.CostEnum;
import com.vegi.vegilabback.model.enums.DifficultyEnum;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Slf4j
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

    private int cookTime;

    private int prepareTime;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date publishDate;

    private int nbPerson;

    private StatusEnum status;

    private DifficultyEnum difficulty;

    private CostEnum cost;

    @ElementCollection
    List<String> steps = new ArrayList<>();

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

/*    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    Set<Preparation> preparations;*/

/*    @OneToMany(mappedBy = "recipe")
    @JsonIgnore
    Set<Planning> plannings;*/

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            })
    @JoinTable(name = "recipe_category",
            joinColumns = { @JoinColumn(name = "recipe_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") })
    private Set<Category> categories = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.MERGE,
                CascadeType.PERSIST
            },
            mappedBy = "liked")
    @JsonIgnore
    private Set<User> likes = new HashSet<>();

    public Recipe() {

    }


    public void addCategory(Category cat) {
        this.categories.add(cat);
        cat.getRecipes().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getNbPerson() {
        return nbPerson;
    }

    public void setNbPerson(int nbPerson) {
        this.nbPerson = nbPerson;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    public CostEnum getCost() {
        return cost;
    }

    public void setCost(CostEnum cost) {
        this.cost = cost;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }
}
