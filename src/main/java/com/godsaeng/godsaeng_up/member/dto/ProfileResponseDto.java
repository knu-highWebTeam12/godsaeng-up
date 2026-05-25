package com.godsaeng.godsaeng_up.member.dto;

import com.godsaeng.godsaeng_up.member.entity.Member;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String loginId;
    private int level;
    private int exp;

    public ProfileResponseDto(Member member) {
        this.loginId = member.getLoginId();
        this.level = member.getLevel();
        this.exp = member.getExp();
    }
}