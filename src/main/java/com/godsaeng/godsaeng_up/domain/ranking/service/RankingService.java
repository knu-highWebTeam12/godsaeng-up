package com.godsaeng.godsaeng_up.domain.ranking.service;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.domain.ranking.dto.RankingPageResponse;
import com.godsaeng.godsaeng_up.domain.ranking.dto.RankingRequest;
import com.godsaeng.godsaeng_up.domain.user.entity.User;
import com.godsaeng.godsaeng_up.domain.user.repository.UserRepository;
import com.godsaeng.godsaeng_up.global.level.CharacterType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RankingService {

    private static final int PAGE_SIZE = 10;

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public RankingPageResponse getRankingPage(int page, String loginId) {
        Long currentUserId = resolveCurrentUserId(loginId);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE,
                Sort.by(
                        Sort.Order.desc("level"),
                        Sort.Order.desc("exp"),
                        Sort.Order.asc("id")
                ));

        Page<Profile> rankingPage = profileRepository.findAll(pageable);

        List<RankingRequest> rankings = IntStream.range(0, rankingPage.getNumberOfElements())
                .mapToObj(index -> toRankingItem(
                        rankingPage.getContent().get(index),
                        rankingPage.getNumber(),
                        index,
                        currentUserId
                ))
                .toList();

        return new RankingPageResponse(
                rankings,
                rankingPage.getNumber(),
                rankingPage.getTotalPages(),
                rankingPage.hasPrevious(),
                rankingPage.hasNext(),
                Math.max(0, rankingPage.getNumber() - 1),
                Math.min(Math.max(0, rankingPage.getTotalPages() - 1), rankingPage.getNumber() + 1),
                createPageNumbers(rankingPage)
        );
    }

    private RankingRequest toRankingItem(Profile profile, int currentPage, int index, Long currentUserId) {
        CharacterType characterType = CharacterType.fromLevel(profile.getLevel());
        int rank = currentPage * PAGE_SIZE + index + 1;

        return new RankingRequest(
                rank,
                profile.getNickname(),
                profile.getLevel(),
                profile.getExp(),
                characterType.getDisplayName(),
                characterType.getRankingImagePath(),
                currentUserId != null && profile.getUserId().equals(currentUserId)
        );
    }

    private Long resolveCurrentUserId(String loginId) {
        if (loginId == null) {
            return null;
        }

        return userRepository.findByLoginId(loginId)
                .map(User::getId)
                .orElse(null);
    }

    private List<RankingPageResponse.PageNumberItem> createPageNumbers(Page<Profile> rankingPage) {
        int totalPages = rankingPage.getTotalPages();
        if (totalPages == 0) {
            return List.of();
        }

        return IntStream.range(0, totalPages)
                .mapToObj(page -> new RankingPageResponse.PageNumberItem(
                        page,
                        page + 1,
                        page == rankingPage.getNumber()
                ))
                .toList();
    }
}
