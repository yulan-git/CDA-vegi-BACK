package com.vegi.vegilabback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class IngredientList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Ingredient ingredient;

    private int quantity;


}
