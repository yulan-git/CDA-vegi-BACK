package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class RecipeWithFavoritesDto {
    private Long id;
    private Set<UserDtoForUpdate> likes;
}
