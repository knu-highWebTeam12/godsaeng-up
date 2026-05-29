package com.godsaeng.godsaeng_up.domain.home.controller;


import com.godsaeng.godsaeng_up.domain.home.dto.MainHomeRequest;
import com.godsaeng.godsaeng_up.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/")
    public String onboarding(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            return "redirect:/main";
        }

        model.addAttribute("topRankings", homeService.getOnboardingTopRankings());
        return "home/index";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        MainHomeRequest page = homeService.getMainHome(userDetails.getUsername());
        model.addAttribute("nickname", page.nickname());
        model.addAttribute("level", page.level());
        model.addAttribute("characterName", page.characterName());
        model.addAttribute("characterImage", page.characterImage());
        model.addAttribute("exp", page.exp());
        model.addAttribute("requiredExp", page.requiredExp());
        model.addAttribute("progressPercent", page.progressPercent());
        model.addAttribute("today", page.today());
        model.addAttribute("hasTodayMissions", page.hasTodayMissions());
        model.addAttribute("todayMissions", page.todayMissions());
        model.addAttribute("topRankings", page.topRankings());
        return "home/main";
    }
}
