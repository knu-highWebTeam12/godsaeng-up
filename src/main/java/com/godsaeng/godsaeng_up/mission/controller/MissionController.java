package com.godsaeng.godsaeng_up.mission.controller;

import com.godsaeng.godsaeng_up.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 미션 등록
    @PostMapping
    public ResponseEntity<MissionResponseDto> createMission(
            @RequestParam Long memberId,
            @RequestBody MissionRequestDto dto) {
        return ResponseEntity.ok(missionService.createMission(memberId, dto));
    }

    // 오늘 미션 조회
    @GetMapping("/today")
    public ResponseEntity<List<MissionResponseDto>> getTodayMissions(
            @RequestParam Long memberId) {
        return ResponseEntity.ok(missionService.getTodayMissions(memberId));
    }

    // 미션 수정
    @PatchMapping("/{missionId}")
    public ResponseEntity<MissionResponseDto> updateMission(
            @PathVariable Long missionId,
            @RequestBody MissionRequestDto dto) {
        return ResponseEntity.ok(missionService.updateMission(missionId, dto));
    }
}