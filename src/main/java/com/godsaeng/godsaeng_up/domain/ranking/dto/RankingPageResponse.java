package com.godsaeng.godsaeng_up.domain.ranking.dto;

import java.util.List;

public record RankingPageResponse(
        List<RankingRequest> rankings,
        int currentPage,
        int totalPages,
        boolean hasPrevious,
        boolean hasNext,
        int previousPage,
        int nextPage,
        List<PageNumberItem> pageNumbers
) {
    public record PageNumberItem(
            int page,
            int displayPage,
            boolean active
    ) {
    }
}
