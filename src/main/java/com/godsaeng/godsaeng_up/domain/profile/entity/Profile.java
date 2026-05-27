package com.godsaeng.godsaeng_up.domain.profile.entity;

import com.godsaeng.godsaeng_up.global.entity.BaseEntity;
import com.godsaeng.godsaeng_up.global.level.LevelPolicy;
import com.godsaeng.godsaeng_up.global.level.LevelUpResult;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profile")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private int exp = 0;

    public Profile(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public void gainExp(int earnedExp) {
        LevelUpResult result = LevelPolicy.applyExp(level, exp, earnedExp);
        this.exp = result.exp();
        this.level = result.level();
    }
}
