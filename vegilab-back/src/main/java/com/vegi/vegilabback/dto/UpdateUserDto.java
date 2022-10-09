package com.vegi.vegilabback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
