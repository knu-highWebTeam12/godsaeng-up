package com.godsaeng.godsaeng_up.mission.entity;

public enum Difficulty {
    EASY, NORMAL, HARD;

    public int getExp() {
        return switch (this) {
            case EASY -> 10;
            case NORMAL -> 30;
            case HARD -> 50;
        };
    }
}