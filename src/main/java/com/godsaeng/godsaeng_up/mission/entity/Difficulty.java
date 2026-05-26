package com.godsaeng.godsaeng_up.mission.entity;

public enum Difficulty {
    하, 중, 상;

    public int getExp() {
        return switch (this) {
            case 하 -> 10;
            case 중 -> 30;
            case 상 -> 50;
        };
    }
}