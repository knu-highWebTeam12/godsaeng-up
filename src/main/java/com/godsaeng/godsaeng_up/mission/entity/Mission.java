package com.godsaeng.godsaeng_up.mission.entity;

import com.godsaeng.godsaeng_up.global.entity.BaseEntity;
import com.godsaeng.godsaeng_up.member.entity.Member;     // 팀원이 만들 회원 엔티티 (가정)
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty; // EASY, NORMAL, HARD

    @Enumerated(EnumType.STRING)
    private MissionStatus status; // TODO, DONE

    private String imageUrl; // 우리가 방금 만든 파일 저장 경로가 들어갈 곳!

    private LocalDate missionDate;

    private LocalDateTime completedAt;

    private Integer earnedExp;

    private Boolean isModified;

    @Builder
    public Mission(Member member, String content, Difficulty difficulty, LocalDate missionDate) {
        this.member = member;
        this.content = content;
        this.difficulty = difficulty;
        this.status = MissionStatus.TODO; // 처음 미션을 만들 때는 무조건 TODO
        this.missionDate = missionDate;
        this.earnedExp = 0;
        this.isModified = false;
    }

    // 사진 인증을 완료했을 때 상태를 변경하는 핵심 비즈니스 메서드
    public void completeMission(String imageUrl, int earnedExp) {
        this.status = MissionStatus.DONE;
        this.imageUrl = imageUrl;
        this.completedAt = LocalDateTime.now();
        this.earnedExp = earnedExp;
    }
}