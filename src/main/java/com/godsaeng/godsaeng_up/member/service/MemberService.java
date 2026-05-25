package com.godsaeng.godsaeng_up.member.service;

import com.godsaeng.godsaeng_up.member.dto.ProfileResponseDto;
import com.godsaeng.godsaeng_up.member.entity.Member;
import com.godsaeng.godsaeng_up.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public ProfileResponseDto getProfileByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다: " + loginId));

        return new ProfileResponseDto(member);
    }
}