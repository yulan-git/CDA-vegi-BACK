package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);
    Recipe getById(Long id);
    Page<Recipe> findByPublishDate(Date date, Pageable pageable);
    Page<Recipe> findByName(String name, Pageable paging);

    @Query(value = "SELECT vegi.recipes.name, vegi.recipes.cook_time, vegi.recipes.id, vegi.recipes.cost, " +
            "vegi.recipes.description, vegi.recipes.difficulty, vegi.recipes.nb_person, vegi.recipes.prepare_time, " +
            "vegi.recipes.publish_date, vegi.recipes.user_id, vegi.recipes.status, vegi.recipes.url_image" +
            "FROM vegi.categories_list " +
            "RIGHT JOIN vegi.recipes ON recipes.id = categories_list.recipe_id " +
            "WHERE categories_list.category_id=?1 " +
            "ORDER BY recipes.publish_date DESC LIMIT 5", nativeQuery = true)
    List<Recipe> findByCategories(Long categoryId);
}
