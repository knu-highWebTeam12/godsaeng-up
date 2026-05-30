package com.godsaeng.godsaeng_up.domain.mission.dto;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;

import java.time.LocalDate;

public record MissionRequestDto(
        String content,
        Difficulty difficulty,
        LocalDate missionDate
) {
}
