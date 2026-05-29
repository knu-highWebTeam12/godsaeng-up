package com.godsaeng.godsaeng_up.domain.home.dto;

import com.godsaeng.godsaeng_up.domain.mission.dto.MissionResponseDto;

import java.util.List;

public record MainHomeRequest(
    String nickname,
    int level,
    String characterName,
    String characterImage,
    int exp,
    int requiredExp,
    int progressPercent,
    String today,
    boolean hasTodayMissions,
    List<MissionResponseDto> todayMissions,
    List<RankingItem> topRankings
) {
    public record RankingItem(
            int rank,
            String nickname,
            int level,
            int exp,
            String characterName,
            String characterImage,
            boolean isMe
    ) {
    }
}
