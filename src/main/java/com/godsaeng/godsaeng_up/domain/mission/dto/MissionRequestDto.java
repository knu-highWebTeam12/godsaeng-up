package com.godsaeng.godsaeng_up.domain.mission.dto;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MissionRequestDto {
    private String content;
    private Difficulty difficulty;
    private LocalDate missionDate;
}