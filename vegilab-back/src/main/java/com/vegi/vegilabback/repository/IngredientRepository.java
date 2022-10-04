package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.Ingredient;
import com.vegi.vegilabback.model.Role;
import com.vegi.vegilabback.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    public Boolean existsByLabel(String label);
}