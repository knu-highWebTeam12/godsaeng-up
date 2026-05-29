package com.godsaeng.godsaeng_up.domain.profile.dto;

import java.util.List;

public record MissionHistoryCursorResponse(
        List<MissionDateGroupResponse> missionGroups,
        String nextCursor,
        boolean hasNext
) {
}
