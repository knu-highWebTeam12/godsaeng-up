package com.godsaeng.godsaeng_up.domain.mission.service;

import com.godsaeng.godsaeng_up.domain.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.domain.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.domain.mission.entity.Mission;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionStatus;
import com.godsaeng.godsaeng_up.domain.mission.repository.MissionRepository;
import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.domain.user.entity.User;
import com.godsaeng.godsaeng_up.domain.user.repository.UserRepository;
import com.godsaeng.godsaeng_up.global.util.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MissionService {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FileStore fileStore;

    @Transactional
    public MissionResponseDto createMission(String loginId, MissionRequestDto dto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        validateMissionRequest(dto);
        missionRepository.findByUser_IdAndMissionDateAndDifficulty(
                        user.getId(), dto.missionDate(), dto.difficulty())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 등록되어 있습니다.");
                });

        Mission mission = new Mission();
        mission.setUser(user);
        mission.setContent(dto.content().trim());
        mission.setDifficulty(dto.difficulty());
        mission.setMissionDate(dto.missionDate());
        mission.setStatus(MissionStatus.TODO);

        return MissionResponseDto.from(missionRepository.save(mission));
    }

    @Transactional(readOnly = true)
    public MissionResponseDto getMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));
        return MissionResponseDto.from(mission);
    }

    @Transactional(readOnly = true)
    public MissionResponseDto getMissionForUser(Long missionId, String loginId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));
        validateMissionOwner(mission, loginId);
        return MissionResponseDto.from(mission);
    }

    @Transactional
    public MissionResponseDto updateMission(Long missionId, String loginId, MissionRequestDto dto) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));

        validateMissionOwner(mission, loginId);
        validateMissionEditable(mission);
        validateMissionContent(dto);

        mission.setContent(dto.content().trim());
        mission.setModified(true);

        return MissionResponseDto.from(mission);
    }

    @Transactional
    public void uploadMissionProof(Long missionId, String loginId, MultipartFile file) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다."));

        validateMissionOwner(mission, loginId);
        validateMissionProofTarget(mission);
        validateProofFile(file);

        try {
            String storedFileName = fileStore.storeFile(file);
            if (storedFileName == null) {
                throw new IllegalArgumentException("업로드할 사진을 선택해주세요.");
            }

            int earnedExp = mission.getDifficulty().getExp();
            mission.completeMission(fileStore.getFileUrl(storedFileName), earnedExp);

            Profile profile = profileRepository.findByUserId(mission.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
            profile.gainExp(earnedExp);
        } catch (IOException e) {
            throw new IllegalStateException("사진 저장에 실패했습니다.", e);
        }
    }

    private void validateMissionRequest(MissionRequestDto dto) {
        validateMissionContent(dto);
        if (dto.difficulty() == null) {
            throw new IllegalArgumentException("난이도를 선택해주세요.");
        }
        if (dto.missionDate() == null) {
            throw new IllegalArgumentException("미션 날짜가 올바르지 않습니다.");
        }
        if (!LocalDate.now().equals(dto.missionDate())) {
            throw new IllegalArgumentException("오늘 날짜의 미션만 등록할 수 있습니다.");
        }
    }

    private void validateMissionContent(MissionRequestDto dto) {
        if (dto.content() == null || dto.content().isBlank()) {
            throw new IllegalArgumentException("미션 내용을 입력해주세요.");
        }
    }

    private void validateMissionOwner(Mission mission, String loginId) {
        if (!mission.getUser().getLoginId().equals(loginId)) {
            throw new IllegalArgumentException("본인 미션만 처리할 수 있습니다.");
        }
    }

    private void validateMissionEditable(Mission mission) {
        if (mission.isModified()) {
            throw new IllegalArgumentException("미션은 1회만 수정할 수 있습니다.");
        }
        if (mission.getStatus() == MissionStatus.DONE) {
            throw new IllegalArgumentException("완료된 미션은 수정할 수 없습니다.");
        }
    }

    private void validateMissionProofTarget(Mission mission) {
        if (mission.getStatus() == MissionStatus.DONE) {
            throw new IllegalArgumentException("이미 인증이 완료된 미션입니다.");
        }
    }

    private void validateProofFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 사진을 선택해주세요.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("JPG 또는 PNG 파일만 업로드할 수 있습니다.");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("파일 확장자를 확인할 수 없습니다.");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("JPG 또는 PNG 파일만 업로드할 수 있습니다.");
        }
    }
}
