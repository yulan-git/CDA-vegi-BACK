package com.vegi.vegilabback.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CategoriesList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

}
