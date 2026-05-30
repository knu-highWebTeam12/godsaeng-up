package com.godsaeng.godsaeng_up.domain.mission.controller;

import com.godsaeng.godsaeng_up.domain.mission.dto.MissionRequestDto;
import com.godsaeng.godsaeng_up.domain.mission.dto.MissionResponseDto;
import com.godsaeng.godsaeng_up.domain.mission.entity.Difficulty;
import com.godsaeng.godsaeng_up.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/new")
    public String missionForm(Model model) {
        MissionRequestDto missionRequest = new MissionRequestDto("", null, LocalDate.now());
        populateMissionForm(model, missionRequest, null);
        return "mission/mission-form";
    }

    @PostMapping("/new")
    public String createMission(
            @AuthenticationPrincipal UserDetails loginUser,
            @ModelAttribute MissionRequestDto dto,
            Model model) {
        try {
            missionService.createMission(loginUser.getUsername(), dto);
            return "redirect:/main";
        } catch (IllegalArgumentException e) {
            populateMissionForm(model, dto, e.getMessage());
            return "mission/mission-form";
        }
    }

    @GetMapping("/{missionId}/edit")
    public String editForm(@PathVariable Long missionId,
                           @AuthenticationPrincipal UserDetails loginUser,
                           Model model) {
        MissionResponseDto mission = missionService.getMissionForUser(missionId, loginUser.getUsername());
        populateMissionEditForm(model, mission, mission.content(), null);
        return "mission/mission-edit";
    }

    @PostMapping("/{missionId}/edit")
    public String updateMission(
            @PathVariable Long missionId,
            @AuthenticationPrincipal UserDetails loginUser,
            @ModelAttribute MissionRequestDto dto,
            Model model) {
        try {
            missionService.updateMission(missionId, loginUser.getUsername(), dto);
            return "redirect:/main";
        } catch (IllegalArgumentException e) {
            MissionResponseDto mission = missionService.getMissionForUser(missionId, loginUser.getUsername());
            populateMissionEditForm(model, mission, dto.content(), e.getMessage());
            return "mission/mission-edit";
        }
    }

    @GetMapping("/{missionId}/proof")
    public String showUploadForm(@PathVariable Long missionId,
                                 @AuthenticationPrincipal UserDetails loginUser,
                                 Model model) {
        MissionResponseDto mission = missionService.getMissionForUser(missionId, loginUser.getUsername());
        populateProofForm(model, mission, null);
        return "mission/proof";
    }

    @PostMapping("/{missionId}/proof")
    public String uploadProof(
            @PathVariable Long missionId,
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestParam("file") MultipartFile file,
            Model model) {
        try {
            missionService.uploadMissionProof(missionId, loginUser.getUsername(), file);
            return "redirect:/main";
        } catch (IllegalArgumentException | IllegalStateException e) {
            MissionResponseDto mission = missionService.getMissionForUser(missionId, loginUser.getUsername());
            populateProofForm(model, mission, e.getMessage());
            return "mission/proof";
        }
    }

    private void populateMissionForm(Model model, MissionRequestDto missionRequest, String formError) {
        LocalDate today = missionRequest.missionDate() != null ? missionRequest.missionDate() : LocalDate.now();
        model.addAttribute("missionRequest", missionRequest);
        model.addAttribute("difficulties", buildDifficultyOptions(missionRequest.difficulty()));
        model.addAttribute("today", today);
        model.addAttribute("formError", formError);
    }

    private void populateMissionEditForm(Model model,
                                         MissionResponseDto mission,
                                         String contentValue,
                                         String formError) {
        model.addAttribute("mission", mission);
        model.addAttribute("contentValue", contentValue == null ? mission.content() : contentValue);
        model.addAttribute("formError", formError);
    }

    private void populateProofForm(Model model, MissionResponseDto mission, String formError) {
        model.addAttribute("mission", mission);
        model.addAttribute("missionId", mission.id());
        model.addAttribute("formError", formError);
    }

    private DifficultyOption[] buildDifficultyOptions(Difficulty selectedDifficulty) {
        return new DifficultyOption[] {
                new DifficultyOption(Difficulty.EASY.name(), Difficulty.EASY.getDifficulty(), selectedDifficulty == Difficulty.EASY),
                new DifficultyOption(Difficulty.NORMAL.name(), Difficulty.NORMAL.getDifficulty(), selectedDifficulty == Difficulty.NORMAL),
                new DifficultyOption(Difficulty.HARD.name(), Difficulty.HARD.getDifficulty(), selectedDifficulty == Difficulty.HARD)
        };
    }

    private record DifficultyOption(String value, String label, boolean selected) {
    }
}
