package com.godsaeng.godsaeng_up.domain.ranking.controller;

import com.godsaeng.godsaeng_up.domain.ranking.dto.RankingPageResponse;
import com.godsaeng.godsaeng_up.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/ranking")
    public String ranking(@RequestParam(defaultValue = "0") int page,
                          @AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        String loginId = userDetails != null ? userDetails.getUsername() : null;
        RankingPageResponse rankingPage = rankingService.getRankingPage(page, loginId);
        model.addAttribute("rankings", rankingPage.rankings());
        model.addAttribute("currentPage", rankingPage.currentPage());
        model.addAttribute("displayPage", rankingPage.currentPage() + 1);
        model.addAttribute("totalPages", rankingPage.totalPages());
        model.addAttribute("hasPrevious", rankingPage.hasPrevious());
        model.addAttribute("hasNext", rankingPage.hasNext());
        model.addAttribute("previousPage", rankingPage.previousPage());
        model.addAttribute("nextPage", rankingPage.nextPage());
        model.addAttribute("pageNumbers", rankingPage.pageNumbers());
        return "ranking/list";
    }
}
