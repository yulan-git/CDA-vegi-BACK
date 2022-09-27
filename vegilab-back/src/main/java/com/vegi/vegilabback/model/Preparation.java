package com.vegi.vegilabback.model;

import com.vegi.vegilabback.model.enums.UnitEnum;

import javax.persistence.*;

@Entity
public class Preparation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    private int quantity;
    private UnitEnum unit;
}
