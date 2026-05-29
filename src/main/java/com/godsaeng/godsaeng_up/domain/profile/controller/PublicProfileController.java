package com.godsaeng.godsaeng_up.domain.profile.controller;

import com.godsaeng.godsaeng_up.domain.profile.dto.MissionHistoryCursorResponse;
import com.godsaeng.godsaeng_up.domain.profile.dto.PublicProfilePageResponse;
import com.godsaeng.godsaeng_up.domain.profile.service.PublicProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PublicProfileController {

    private final PublicProfileService publicProfileService;

    @GetMapping("/user/{nickname}")
    public String publicProfile(@PathVariable String nickname, Model model) {
        PublicProfilePageResponse page = publicProfileService.getPublicProfilePage(nickname);
        model.addAttribute("nickname", page.nickname());
        model.addAttribute("level", page.level());
        model.addAttribute("exp", page.exp());
        model.addAttribute("characterName", page.characterName());
        model.addAttribute("characterImage", page.characterImage());
        model.addAttribute("missionGroups", page.missionGroups());
        model.addAttribute("nextCursor", page.nextCursor() == null ? "" : page.nextCursor());
        model.addAttribute("hasNext", page.hasNext());
        return "profile/detail";
    }

    @GetMapping("/api/user/{nickname}/missions")
    @ResponseBody
    public ResponseEntity<MissionHistoryCursorResponse> missionHistory(
            @PathVariable String nickname,
            @RequestParam(required = false) String cursor) {
        return ResponseEntity.ok(publicProfileService.getMissionHistory(nickname, cursor));
    }
}
