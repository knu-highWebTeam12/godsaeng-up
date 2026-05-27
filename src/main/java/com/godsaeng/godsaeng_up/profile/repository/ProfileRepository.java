package com.godsaeng.godsaeng_up.profile.repository;

import com.godsaeng.godsaeng_up.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
}