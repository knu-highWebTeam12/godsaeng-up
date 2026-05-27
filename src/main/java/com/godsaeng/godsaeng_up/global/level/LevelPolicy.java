package com.godsaeng.godsaeng_up.global.level;

public final class LevelPolicy {

    private static final int BASE_REQUIRED_EXP = 100;
    private static final int LEVEL_INCREMENT_EXP = 10;

    private LevelPolicy() {
    }

    public static int requiredExpFor(int level) {
        return BASE_REQUIRED_EXP + ((level - 1) * LEVEL_INCREMENT_EXP);
    }

    public static LevelUpResult applyExp(int currentLevel, int currentExp, int earnedExp) {
        int level = currentLevel;
        int exp = currentExp + earnedExp;

        //경험치가 2레벨 이상을 상회할 수 있을 경우를 생각해 반복(기획 상 그럴일 없긴 함)
        while (exp >= requiredExpFor(level)) {
            exp -= requiredExpFor(level);
            level++;
        }
        return new LevelUpResult(level, exp);
    }

    public static int progressPercent(int level, int exp) {
        int requiredExp = requiredExpFor(level);
        return (int) ((exp * 100.0) / requiredExp);
    }
}
