package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.model.IngredientList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientListRepository extends JpaRepository<IngredientList, Long> {
}
