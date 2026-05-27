package com.godsaeng.godsaeng_up.domain.mission.entity;

public enum Difficulty {
    EASY(10), NORMAL(30), HARD(50);

    private final int exp; // 난이도별 획득 경험치

    Difficulty(int exp) {
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }
}