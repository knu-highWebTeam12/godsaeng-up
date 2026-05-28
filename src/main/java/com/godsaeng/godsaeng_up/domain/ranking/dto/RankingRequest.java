package com.godsaeng.godsaeng_up.domain.ranking.dto;

public record RankingRequest(
        int rank,
        String nickname,
        int level,
        int exp,
        String characterName,
        boolean isMe
) {
}
