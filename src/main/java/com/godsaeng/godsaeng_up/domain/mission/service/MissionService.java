package com.godsaeng.godsaeng_up.domain.mission.service;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.global.util.FileStore;
import com.godsaeng.godsaeng_up.domain.mission.entity.Mission;
import com.godsaeng.godsaeng_up.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final ProfileRepository profileRepository;
    private final FileStore fileStore;

    /**
     * 미션 사진 인증 완료 처리 로직
     */
    @Transactional
    public void uploadMissionProof(Long missionId, MultipartFile file) throws IOException {
        // 1. 데이터베이스에서 미션 찾기
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미션입니다. ID: " + missionId));

        // 2. 내 컴퓨터에 사진 저장하기
        String storeFileName = fileStore.storeFile(file);

        if (storeFileName != null) {
            // 3. 미션 경험치 가져오기 및 미션 완료 처리
            int exp = mission.getDifficulty().getExp();
            mission.completeMission(storeFileName, exp);

            Profile profile = profileRepository.findByUserId(mission.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
            profile.gainExp(exp);

        } else {
            throw new IllegalArgumentException("업로드된 인증 사진 파일이 없습니다.");
        }
    }
}
