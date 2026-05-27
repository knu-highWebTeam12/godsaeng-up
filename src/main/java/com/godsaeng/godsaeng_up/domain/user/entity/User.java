package com.godsaeng.godsaeng_up.domain.user.entity;

import com.godsaeng.godsaeng_up.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "user_account",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_account_login_id", columnNames = "login_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 30)
    private String loginId;

    @Column(nullable = false)
    private String password;

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }


}