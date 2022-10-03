package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.Recipe;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
public class SimpleRecipeDto {
    private Long id;
    private String name;
    private String description;
    private String urlImage;

}
