package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.CategoriesList;
import com.vegi.vegilabback.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesListRepository extends JpaRepository<CategoriesList, Long> {
    List<CategoriesList> getCategoriesListByRecipe(Recipe recipe);
}
