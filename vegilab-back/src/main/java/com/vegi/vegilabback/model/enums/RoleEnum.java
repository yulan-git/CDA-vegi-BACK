package com.vegi.vegilabback.model.enums;

public enum RoleEnum {
    ADMIN("admin"),
    USER("user"),
    MODERATOR("moderator");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
