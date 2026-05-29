package com.godsaeng.godsaeng_up.domain.mission.entity;

import lombok.Getter;

@Getter
public enum Difficulty {
    EASY("하", 10),
    NORMAL("중", 30),
    HARD("상", 50);

    private final String difficulty;
    private final int exp;

    Difficulty(String difficulty, int exp) {
        this.difficulty = difficulty;
        this.exp = exp;
    }

    public String difficultyClass(Difficulty difficulty) {
        return switch (difficulty) {
            case HARD -> "text-bg-danger";
            case NORMAL -> "text-bg-warning";
            case EASY -> "text-bg-primary";
        };
    }
}