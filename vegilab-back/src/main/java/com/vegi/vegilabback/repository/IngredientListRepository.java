package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.IngredientList;
import com.vegi.vegilabback.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientListRepository extends JpaRepository<IngredientList, Long> {
    @Modifying
    @Query(value = "DELETE FROM vegi.ingredient_list WHERE vegi.ingredient_list.id=?1", nativeQuery = true)
    void deleteIngredientListByRecipe(Long id);

    @Query(value = "SELECT * FROM vegi.ingredient_list WHERE vegi.ingredient_list.recipe_id=?1", nativeQuery = true)
    List<IngredientList> findIdListByRecipe(Long id);
}
