package com.godsaeng.godsaeng_up.domain.mission.dto;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionEntity;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MissionResponseDto {
    private Long id;
    private String content;
    private Difficulty difficulty;
    private MissionStatus status;
    private String imageUrl;
    private LocalDate missionDate;
    private LocalDateTime completedAt;
    private int earnedExp;
    private boolean isModified;
    private boolean isTodo;

    public MissionResponseDto(MissionEntity mission) {
        this.id = mission.getId();
        this.content = mission.getContent();
        this.difficulty = mission.getDifficulty();
        this.status = mission.getStatus();
        this.imageUrl = mission.getImageUrl();
        this.missionDate = mission.getMissionDate();
        this.completedAt = mission.getCompletedAt();
        this.earnedExp = mission.getEarnedExp();
        this.isModified = mission.isModified();
        this.isTodo = mission.getStatus() == MissionStatus.TODO;
    }
}