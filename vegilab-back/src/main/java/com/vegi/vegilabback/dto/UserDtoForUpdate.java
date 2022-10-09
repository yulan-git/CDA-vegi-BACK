package com.vegi.vegilabback.dto;

import com.vegi.vegilabback.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDtoForUpdate {
        private Long id;
        private String username;
}
