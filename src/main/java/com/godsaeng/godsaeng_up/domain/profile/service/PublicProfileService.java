package com.godsaeng.godsaeng_up.domain.profile.service;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.entity.Mission;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionStatus;
import com.godsaeng.godsaeng_up.domain.mission.repository.MissionRepository;
import com.godsaeng.godsaeng_up.domain.profile.dto.MissionDateGroupResponse;
import com.godsaeng.godsaeng_up.domain.profile.dto.MissionHistoryCursorResponse;
import com.godsaeng.godsaeng_up.domain.profile.dto.MissionItemResponse;
import com.godsaeng.godsaeng_up.domain.profile.dto.PublicProfilePageResponse;
import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.global.level.CharacterType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicProfileService {

    private static final int DATE_BATCH_SIZE = 5;
    private static final String EMPTY_MISSION_IMAGE = "/images/placeholders/mission-empty.svg";

    private final ProfileRepository profileRepository;
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public PublicProfilePageResponse getPublicProfilePage(String nickname) {
        Profile profile = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        CharacterType characterType = CharacterType.fromLevel(profile.getLevel());
        MissionHistoryCursorResponse history = getMissionHistory(nickname, null);

        return new PublicProfilePageResponse(
                profile.getNickname(),
                profile.getLevel(),
                profile.getExp(),
                characterType.getDisplayName(),
                characterType.getImagePath(),
                history.missionGroups(),
                history.nextCursor(),
                history.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public MissionHistoryCursorResponse getMissionHistory(String nickname, String cursor) {
        Profile profile = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        LocalDate cursorDate = cursor == null || cursor.isBlank()
                ? LocalDate.now().plusDays(1)
                : LocalDate.parse(cursor);

        List<LocalDate> fetchedDates = missionRepository.findDistinctMissionDatesBefore(
                profile.getUserId(),
                cursorDate,
                PageRequest.of(0, DATE_BATCH_SIZE + 1)
        );

        boolean hasNext = fetchedDates.size() > DATE_BATCH_SIZE;
        List<LocalDate> dates = hasNext ? fetchedDates.subList(0, DATE_BATCH_SIZE) : fetchedDates;
        String nextCursor = hasNext && !dates.isEmpty() ? dates.get(dates.size() - 1).toString() : "";

        if (dates.isEmpty()) {
            return new MissionHistoryCursorResponse(List.of(), "", false);
        }

        List<Mission> missions = missionRepository.findByUser_IdAndMissionDateIn(profile.getUserId(), dates);
        Map<LocalDate, Map<Difficulty, Mission>> missionMap = missions.stream()
                .collect(Collectors.groupingBy(
                        Mission::getMissionDate,
                        Collectors.toMap(
                                Mission::getDifficulty,
                                mission -> mission,
                                (left, right) -> left,
                                () -> new EnumMap<>(Difficulty.class)
                        )
                ));

        List<MissionDateGroupResponse> groups = dates.stream()
                .sorted(Comparator.reverseOrder())
                .map(date -> toDateGroup(date, missionMap.getOrDefault(date, new EnumMap<>(Difficulty.class))))
                .toList();

        return new MissionHistoryCursorResponse(groups, nextCursor, hasNext);
    }

    private MissionDateGroupResponse toDateGroup(LocalDate date, Map<Difficulty, Mission> missionsByDifficulty) {
        List<MissionItemResponse> items = List.of(
                toMissionItem(Difficulty.HARD, missionsByDifficulty.get(Difficulty.HARD)),
                toMissionItem(Difficulty.NORMAL, missionsByDifficulty.get(Difficulty.NORMAL)),
                toMissionItem(Difficulty.EASY, missionsByDifficulty.get(Difficulty.EASY))
        );

        return new MissionDateGroupResponse(date.toString(), items);
    }

    private MissionItemResponse toMissionItem(Difficulty difficulty, Mission mission) {
        boolean completed = mission != null && mission.getStatus() == MissionStatus.DONE;

        return new MissionItemResponse(
                difficulty.getDifficulty(),
                difficulty.difficultyClass(difficulty),
                completed ? mission.getContent() : "수행하지 않음",
                completed && mission.getImageUrl() != null ? mission.getImageUrl() : EMPTY_MISSION_IMAGE,
                completed
        );
    }

}
