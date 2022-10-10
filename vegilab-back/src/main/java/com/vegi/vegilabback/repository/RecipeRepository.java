package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.dto.SimpleRecipeDto;
import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = "SELECT*FROM vegi.recipes RIGHT JOIN vegi.recipe_category ON vegi.recipes.id = vegi.recipe_category.recipe_id WHERE recipe_category.category_id=?1 ", nativeQuery = true)
    Page<Recipe> findByCategoryId(Long id, Pageable paging);

    Page<Recipe> findAll(Pageable pageable);
    Recipe getById(Long id);
    Page<Recipe> findByName(String name, Pageable paging);

    @Query(value = "SELECT *"+
            "FROM vegi.recipe_category " +
            "RIGHT JOIN vegi.recipes ON recipes.id = recipe_category.recipe_id " +
            "WHERE recipe_category.category_id=?1 " +
            "ORDER BY recipes.publish_date DESC LIMIT 5", nativeQuery = true)
    List<Recipe> findByCategories(Long categoryId);

    @Query(value = "SELECT *\n" +
            "            FROM vegi.recipes \n" +
            "            RIGHT JOIN vegi.users ON vegi.recipes.user_id = vegi.users.id \n" +
            "            WHERE users.id=?1 ORDER BY\n" +
            "    vegi.recipes.id DESC\n" +
            "LIMIT 5", nativeQuery = true)
    Page<Recipe> findByUserId(Long id, Pageable paging);

    @Query(value = "SELECT * " +
            "FROM vegi.recipes " +
            "RIGHT JOIN vegi.users ON vegi.recipes.user_id = vegi.users.id " +
            "WHERE users.id=?1 ", nativeQuery = true)
    List<Recipe> findAllByUser(Long id);

}
