package com.godsaeng.godsaeng_up.domain.mission.entity;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mission",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"member_id", "mission_date", "difficulty"}
        ))
@Getter
@Setter
@NoArgsConstructor

public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Profile member;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)

    @Column(nullable = false)

    private MissionStatus status = MissionStatus.TODO;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDate missionDate;

    private LocalDateTime completedAt;

    private int earnedExp;

    private boolean isModified = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}

