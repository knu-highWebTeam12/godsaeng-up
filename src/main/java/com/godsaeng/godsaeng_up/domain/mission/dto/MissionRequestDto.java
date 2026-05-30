package com.godsaeng.godsaeng_up.domain.mission.dto;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class MissionRequestDto {
    private String content;
    private Difficulty difficulty;
    private LocalDate missionDate;
}