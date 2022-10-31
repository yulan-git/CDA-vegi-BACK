package com.vegi.vegilabback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vegi.vegilabback.model.enums.UnitEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    private UnitEnum unit;


}
