package com.godsaeng.godsaeng_up.global.level;

import lombok.Getter;

@Getter
public enum CharacterType {

    EGG(1, 5, "알", "/images/characters/egg.png", "/images/characters/ranking/egg.png"),
    CHICK(6, 10, "병아리", "/images/characters/chick.png", "/images/characters/ranking/chick.png"),
    HAMSTER(11, 15, "햄스터", "/images/characters/hamster.png", "/images/characters/ranking/hamster.png"),
    RABBIT(16, 20, "토끼", "/images/characters/rabbit.png", "/images/characters/ranking/rabbit.png"),
    DEER(21, 25, "사슴", "/images/characters/deer.png", "/images/characters/ranking/deer.png"),
    FOX(26, 30, "여우", "/images/characters/fox.png", "/images/characters/ranking/fox.png"),
    WOLF(31, 35, "늑대", "/images/characters/wolf.png", "/images/characters/ranking/wolf.png"),
    CROCODILE(36, 40, "악어", "/images/characters/crocodile.png", "/images/characters/ranking/crocodile.png"),
    LION(41, 45, "사자", "/images/characters/lion.png", "/images/characters/ranking/lion.png"),
    TIGER(46, 50, "호랑이", "/images/characters/tiger.png", "/images/characters/ranking/tiger.png"),
    BEAR(51, 55, "곰", "/images/characters/bear.png", "/images/characters/ranking/bear.png"),
    ELEPHANT(56, 60, "코끼리", "/images/characters/elephant.png", "/images/characters/ranking/elephant.png"),
    UNICORN(61, 65, "유니콘", "/images/characters/unicorn.png", "/images/characters/ranking/unicorn.png"),
    DRAGON(66, 70, "드래곤", "/images/characters/dragon.png", "/images/characters/ranking/dragon.png"),
    FLAME(71, 75, "불꽃", "/images/characters/flame.png", "/images/characters/ranking/flame.png"),
    LIGHTNING(76, 80, "번개", "/images/characters/lightning.png", "/images/characters/ranking/lightning.png"),
    COMET(81, 85, "혜성", "/images/characters/comet.png", "/images/characters/ranking/comet.png"),
    NEBULA(86, 90, "성운", "/images/characters/nebula.png", "/images/characters/ranking/nebula.png"),
    SUPERNOVA(91, 95, "초신성", "/images/characters/supernova.png", "/images/characters/ranking/supernova.png"),
    TRANSCENDENT(96, 99, "초월자", "/images/characters/transcendent.png", "/images/characters/ranking/transcendent.png"),
    GODSAENGLER(100, Integer.MAX_VALUE, "갓생러", "/images/characters/godsaengler.png", "/images/characters/ranking/godsaengler.png");

    private final int minLevel;
    private final int maxLevel;
    private final String displayName;
    private final String imagePath;
    private final String rankingImagePath;

    CharacterType(int minLevel, int maxLevel, String displayName, String imagePath, String rankingImagePath) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.rankingImagePath = rankingImagePath;
    }

    public static CharacterType fromLevel(int level) {
        for (CharacterType characterType : values()) {
            if (characterType.matches(level)) {
                return characterType;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 레벨입니다. level=" + level);
    }

    private boolean matches(int level) {
        return level >= minLevel && level <= maxLevel;
    }
}
