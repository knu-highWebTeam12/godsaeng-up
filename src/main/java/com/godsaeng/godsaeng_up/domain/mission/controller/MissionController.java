package com.godsaeng.godsaeng_up.domain.mission.controller;

import com.godsaeng.godsaeng_up.domain.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 미션 등록
    @GetMapping("/new")
    public String missionForm(Model model) {
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("today", LocalDate.now().toString());
        return "mission/mission-form";
    }

    // 오늘 미션 조회
    @PostMapping("/new")
    public String createMission(
            @AuthenticationPrincipal UserDetails loginUser,
            @ModelAttribute MissionRequestDto dto) {
        missionService.createMission(loginUser.getUsername(), dto);
        return "redirect:/main";
    }

    // 미션 수정
    @GetMapping("/{missionId}/edit")
    public String editForm(@PathVariable Long missionId, Model model) {
        model.addAttribute("mission", missionService.getMission(missionId));
        return "mission/mission-edit";
    }

    @PostMapping("/{missionId}/edit")
    public String updateMission(
            @PathVariable Long missionId,
            @AuthenticationPrincipal UserDetails loginUser,
            @ModelAttribute MissionRequestDto dto) {
        missionService.updateMission(missionId, loginUser.getUsername(), dto);
        return "redirect:/main";
    }
}