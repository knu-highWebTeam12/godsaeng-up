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
        // 1. 데이터베이스에서 해당 미션이 존재하는지 찾기
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미션입니다. ID: " + missionId));

        // 2. 파일 스토어를 사용해서 내 컴퓨터(C:/uploads/mission/)에 사진 진짜로 저장하기
        String storeFileName = fileStore.storeFile(file);

        if (storeFileName != null) {
            // 3. 미션 난이도(EASY, NORMAL, HARD)에 따른 경험치(EXP) 가져오기
            int exp = mission.getDifficulty().getExp();

            // 4. 미션 엔티티 내부의 데이터 변경 (상태를 DONE으로 바꾸고, 파일명 저장하고, 경험치 세팅)
            mission.completeMission(storeFileName, exp);

            // 영속성 컨텍스트와 @Transactional 덕분에 따로 save()를 명시하지 않아도 DB에 자동으로 업데이트 쿼리가 날아갑니다!
        } else {
            throw new IllegalArgumentException("업로드된 인증 사진 파일이 없습니다.");
        }
    }
}