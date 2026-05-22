package com.godsaeng.godsaeng_up.mission.service;

import com.godsaeng.godsaeng_up.global.util.FileStore;
import com.godsaeng.godsaeng_up.mission.entity.Mission;
import com.godsaeng.godsaeng_up.mission.repository.MissionRepository;
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

            // 미션을 작성한 유저를 데려와서 계산된 경험치를 지갑에 넣어줍니다. (레벨업은 Member 안에서 자동 계산됨)
            mission.getMember().addExp(exp);

        } else {
            throw new IllegalArgumentException("업로드된 인증 사진 파일이 없습니다.");
        }
    }
}