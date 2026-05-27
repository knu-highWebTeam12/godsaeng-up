package com.godsaeng.godsaeng_up.mission.service;

import com.godsaeng.godsaeng_up.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.mission.entity.MissionEntity;
import com.godsaeng.godsaeng_up.mission.entity.MissionStatus;
import com.godsaeng.godsaeng_up.mission.repository.MissionRepository;
import com.godsaeng.godsaeng_up.profile.entity.ProfileEntity;
import com.godsaeng.godsaeng_up.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public MissionResponseDto createMission(Long memberId, MissionRequestDto dto) {

        missionRepository.findByMemberIdAndMissionDateAndDifficulty(
                        memberId, dto.getMissionDate(), dto.getDifficulty())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 해당 난이도의 미션이 등록되어 있습니다.");
                });

        ProfileEntity member = profileRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        MissionEntity mission = new MissionEntity();
        mission.setMember(member);
        mission.setContent(dto.getContent());
        mission.setDifficulty(dto.getDifficulty());
        mission.setMissionDate(dto.getMissionDate());
        mission.setStatus(MissionStatus.TODO);

        return new MissionResponseDto(missionRepository.save(mission));
    }

    // 오늘 미션 조회
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getTodayMissions(Long memberId) {
        List<MissionEntity> missions = missionRepository
                .findByMemberIdAndMissionDate(memberId, LocalDate.now());
        return missions.stream()
                .map(MissionResponseDto::new)
                .collect(Collectors.toList());
    }

    // 미션 수정 (수정 정책: 1회만 가능)
    @Transactional
    public MissionResponseDto updateMission(Long missionId, MissionRequestDto dto) {
        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));

        if (mission.isModified()) {
            throw new IllegalStateException("미션은 1회만 수정할 수 있습니다.");
        }

        if (mission.getStatus() == MissionStatus.DONE) {
            throw new IllegalStateException("완료된 미션은 수정할 수 없습니다.");
        }

        mission.setContent(dto.getContent());
        mission.setModified(true);

        return new MissionResponseDto(mission);
    }
}
