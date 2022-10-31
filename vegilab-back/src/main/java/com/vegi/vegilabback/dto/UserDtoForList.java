package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoForList {
    private String username;
    private String email;
    private Role role;
}
