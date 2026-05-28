package com.godsaeng.godsaeng_up.domain.mission.controller;

import com.godsaeng.godsaeng_up.domain.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.domain.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 미션 등록
    @PostMapping
    public ResponseEntity<MissionResponseDto> createMission(
            @RequestParam Long userId,
            @RequestBody MissionRequestDto dto) {
        return ResponseEntity.ok(missionService.createMission(userId, dto));
    }

    // 오늘 미션 조회
    @GetMapping("/today")
    public ResponseEntity<List<MissionResponseDto>> getTodayMissions(
            @RequestParam Long userId) {
        return ResponseEntity.ok(missionService.getTodayMissions(userId));
    }

    // 미션 수정
    @PatchMapping("/{missionId}")
    public ResponseEntity<MissionResponseDto> updateMission(
            @PathVariable Long missionId,
            @RequestBody MissionRequestDto dto) {
        return ResponseEntity.ok(missionService.updateMission(missionId, dto));
    }

    @GetMapping("/new")
    public String missionForm(Model model, @RequestParam Long userId) {
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("userId", userId);
        return "mission/mission-form";
    }
}