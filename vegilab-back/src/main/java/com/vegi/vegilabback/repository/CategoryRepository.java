package com.vegi.vegilabback.repository;

import com.vegi.vegilabback.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Boolean existsByLabel(String label);
    public Category getById(Long id);
    public Category getByLabel(String label);
    List<Category> findCatsByRecipesId(Long recId);
}
