package com.godsaeng.godsaeng_up.domain.profile.repository;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
    java.util.Optional<Profile> findByUserId(Long userId);
}
