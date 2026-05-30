package com.godsaeng.godsaeng_up.domain.mission.dto;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.entity.Mission;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MissionResponseDto(
        Long id,
        String content,
        Difficulty difficulty,
        MissionStatus status,
        String imageUrl,
        LocalDate missionDate,
        LocalDateTime completedAt,
        int earnedExp,
        boolean isModified,
        boolean isTodo
) {
    public static MissionResponseDto from(Mission mission) {
        return new MissionResponseDto(
                mission.getId(),
                mission.getContent(),
                mission.getDifficulty(),
                mission.getStatus(),
                mission.getImageUrl(),
                mission.getMissionDate(),
                mission.getCompletedAt(),
                mission.getEarnedExp(),
                mission.isModified(),
                mission.getStatus() == MissionStatus.TODO
        );
    }
}
