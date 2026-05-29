package com.godsaeng.godsaeng_up.domain.home.service;

import com.godsaeng.godsaeng_up.domain.home.dto.MainHomeRequest;
import com.godsaeng.godsaeng_up.domain.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.domain.mission.repository.MissionRepository;
import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.domain.user.entity.User;
import com.godsaeng.godsaeng_up.domain.user.repository.UserRepository;
import com.godsaeng.godsaeng_up.global.level.CharacterType;
import com.godsaeng.godsaeng_up.global.level.LevelPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public MainHomeRequest getMainHome(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        CharacterType characterType = CharacterType.fromLevel(profile.getLevel());
        int requiredExp = LevelPolicy.requiredExpFor(profile.getLevel());
        int progressPercent = LevelPolicy.progressPercent(profile.getLevel(), profile.getExp());

        List<MissionResponseDto> todayMissions = missionRepository.findByUser_IdAndMissionDate(user.getId(), LocalDate.now())
                .stream()
                .map(MissionResponseDto::new)
                .toList();

        List<MainHomeRequest.RankingItem> topRankings = getTopRankings(user.getId());

        return new MainHomeRequest(
                profile.getNickname(),
                profile.getLevel(),
                characterType.getDisplayName(),
                characterType.getImagePath(),
                profile.getExp(),
                requiredExp,
                progressPercent,
                LocalDate.now().toString(),
                !todayMissions.isEmpty(),
                todayMissions,
                topRankings
        );
    }

    @Transactional(readOnly = true)
    public List<MainHomeRequest.RankingItem> getOnboardingTopRankings() {
        return getTopRankings(null);
    }

    private List<MainHomeRequest.RankingItem> getTopRankings(Long currentUserId) {
        List<Profile> topProfiles = profileRepository.findAll(
                PageRequest.of(0, 5,
                        Sort.by(
                                Sort.Order.desc("level"),
                                Sort.Order.desc("exp"),
                                Sort.Order.asc("id")
                        ))
        ).getContent();

        return IntStream.range(0, topProfiles.size())
                .mapToObj(index -> {
                    Profile rankingProfile = topProfiles.get(index);
                    CharacterType rankingCharacter = CharacterType.fromLevel(rankingProfile.getLevel());

                    return new MainHomeRequest.RankingItem(
                            index + 1,
                            rankingProfile.getNickname(),
                            rankingProfile.getLevel(),
                            rankingProfile.getExp(),
                            rankingCharacter.getDisplayName(),
                            rankingCharacter.getRankingImagePath(),
                            currentUserId != null && rankingProfile.getUserId().equals(currentUserId)
                    );
                })
                .toList();
    }
}
