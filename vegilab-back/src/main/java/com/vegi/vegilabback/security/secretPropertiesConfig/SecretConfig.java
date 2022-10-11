package com.vegi.vegilabback.security.secretPropertiesConfig;

import com.vegi.vegilabback.model.Role;
import com.vegi.vegilabback.model.enums.RoleEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Data
public class SecretConfig {
    private String role;
    private String  username;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
