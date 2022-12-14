package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.Planning;
import com.vegi.vegilabback.model.Recipe;
import com.vegi.vegilabback.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;

}
