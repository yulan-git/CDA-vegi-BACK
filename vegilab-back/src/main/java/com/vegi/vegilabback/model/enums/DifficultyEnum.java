package com.vegi.vegilabback.model.enums;

public enum DifficultyEnum {
    TRES_FACILE("Très facile"),
    FACILE("Facile"),
    MOYEN("Moyen"),
    EXPERT("Expert");

    private final String difficulty;

    DifficultyEnum(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
