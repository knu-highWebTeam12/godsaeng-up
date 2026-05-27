package com.godsaeng.godsaeng_up.domain.mission.repository;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    // 오늘 미션 전체 조회
    List<MissionEntity> findByMember_IdAndMissionDate(Long memberId, LocalDate date);

    // 중복 등록 방지용
    Optional<MissionEntity> findByMember_IdAndMissionDateAndDifficulty(
            Long memberId, LocalDate date, Difficulty difficulty);
}