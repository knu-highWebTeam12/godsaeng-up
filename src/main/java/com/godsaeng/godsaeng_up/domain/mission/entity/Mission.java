package com.godsaeng.godsaeng_up.domain.mission.entity;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "missions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "mission_date", "difficulty"}
        ))
@Getter
@Setter
@NoArgsConstructor

public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

