package com.godsaeng.godsaeng_up.member.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;

    // --- 새로 추가된 경험치와 레벨 필드 ---
    private int level = 1; // 갓생 RPG 니까 기본 레벨은 1부터 시작!
    private int exp = 0;   // 경험치는 0부터 시작

    // 경험치를 획득하고, 100이 넘으면 레벨업을 시키는 비즈니스 메서드
    public void addExp(int earnedExp) {
        this.exp += earnedExp;

        // 경험치가 100 이상일 때마다 레벨 1 증가, 남은 경험치는 이월됨 (임의로 100을 최대치로 설정)
        while (this.exp >= 100) {
            this.level++;
            this.exp -= 100;
        }
    }
}