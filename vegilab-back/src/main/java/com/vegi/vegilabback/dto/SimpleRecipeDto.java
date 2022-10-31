package com.vegi.vegilabback.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRecipeDto {
    private Long id;
    private String name;
    private String description;
    private String urlImage;
    private StatusEnum status;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate publishDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UserDto user;

}
