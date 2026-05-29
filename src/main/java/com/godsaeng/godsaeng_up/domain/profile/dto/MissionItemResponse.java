package com.godsaeng.godsaeng_up.domain.profile.dto;

public record MissionItemResponse(
        String difficultyLabel,
        String difficultyClass,
        String content,
        String imageUrl,
        boolean completed
) {
}
