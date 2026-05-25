package com.godsaeng.godsaeng_up.member.controller;

import com.godsaeng.godsaeng_up.member.dto.ProfileResponseDto;
import com.godsaeng.godsaeng_up.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profile")
    public String searchProfile(@RequestParam("nickname") String nickname, Model model) {
        try {
            ProfileResponseDto profile = memberService.getProfileByLoginId(nickname);
            model.addAttribute("profile", profile);
            return "member/profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/search";
        }
    }
}