package com.vegi.vegilabback.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.vegi.vegilabback.model.*;
import com.vegi.vegilabback.model.enums.RoleEnum;
import com.vegi.vegilabback.repository.CategoryRepository;
import com.vegi.vegilabback.repository.IngredientRepository;
import com.vegi.vegilabback.repository.RoleRepository;
import com.vegi.vegilabback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class Generator implements CommandLineRunner {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public void run(String... args) throws Exception {
        generateData();
    }

    private void generateData() throws IOException {
        String filePath = "src/main/resources/data.json";
        byte[] bytesFile = Files.readAllBytes(Paths.get(filePath));

        JsonIterator iter = JsonIterator.parse(bytesFile);
        Any any = iter.readAny();

        Any categoryAny = any.get("categories");
        categoryAny.forEach(a -> {
            Category category = new Category();
            category.setLabel(a.get("label").toString());
            category.setAdded(true);
            this.categoryRepository.save(category);
        });

        Any ingrAny = any.get("aliments");
        ingrAny.forEach(a -> {
            Ingredient ingredient = new Ingredient();
            var label = a.get("label").toString().substring(0, 1).toUpperCase() + a.get("label").toString().substring(1).toLowerCase();
            ingredient.setLabel(label);
            ingredient.setAdded(true);
            this.ingredientRepository.save(ingredient);
        });

        Any roleAny = any.get("roles");
        for (Any a : roleAny) {
            String name = a.get("name").toString();
            RoleEnum roles = null;
            if(name.equals("USER")){
                roles = RoleEnum.USER;
            } else if(name.equals("ADMIN")){
                roles = RoleEnum.ADMIN;
            } else {
                roles = RoleEnum.MODERATOR;
            }
            Role role = new Role(
                    roles
            );
            this.roleRepository.save(role);
        };

    }
}
