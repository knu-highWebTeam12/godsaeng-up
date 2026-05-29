package com.godsaeng.godsaeng_up.domain.profile.dto;

import java.util.List;

public record PublicProfilePageResponse(
        String nickname,
        int level,
        int exp,
        String characterName,
        String characterImage,
        List<MissionDateGroupResponse> missionGroups,
        String nextCursor,
        boolean hasNext
) {
}
