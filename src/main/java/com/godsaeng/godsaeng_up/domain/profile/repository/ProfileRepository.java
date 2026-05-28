package com.godsaeng.godsaeng_up.domain.profile.repository;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
    Optional<Profile> findByUserId(Long userId);
}
