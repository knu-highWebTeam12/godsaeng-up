package com.godsaeng.godsaeng_up.domain.mission.repository;

import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    // 오늘 미션 전체 조회
    List<Mission> findByUser_IdAndMissionDate(Long userId, LocalDate date);

    // 중복 등록 방지용
    Optional<Mission> findByUser_IdAndMissionDateAndDifficulty(
            Long userId, LocalDate date, Difficulty difficulty);
}