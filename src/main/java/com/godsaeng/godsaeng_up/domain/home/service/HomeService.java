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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
                Collections.emptyList()
        );
    }
}
