package com.godsaeng.godsaeng_up.mission.repository;

import com.godsaeng.godsaeng_up.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // JpaRepository를 상속받으면 findById(), save() 같은 기본 메서드를 그냥 쓸 수 있어요!
}