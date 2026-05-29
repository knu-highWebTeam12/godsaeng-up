package com.godsaeng.godsaeng_up.domain.profile.dto;

import java.util.List;

public record MissionDateGroupResponse(
        String date,
        List<MissionItemResponse> missions
) {
}
