package com.godsaeng.godsaeng_up.mission.controller;

import com.godsaeng.godsaeng_up.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j // 로그를 찍기 위한 어노테이션
@Controller
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 1. 사진 업로드 화면(폼)을 보여주는 메서드
    @GetMapping("/{missionId}/proof")
    public String showUploadForm(@PathVariable Long missionId, Model model) {
        model.addAttribute("missionId", missionId);
        // 팀장님 지시대로 Mustache 템플릿(HTML)을 반환합니다.
        return "mission/proof";
    }

    // 2. 사용자가 사진을 전송했을 때 처리하는 핵심 메서드
    @PostMapping("/{missionId}/proof")
    public String uploadProof(
            @PathVariable Long missionId,
            @RequestParam("file") MultipartFile file,
            Model model) {

        try {
            log.info("미션 ID {}의 사진 인증 요청이 들어왔습니다.", missionId);

            // 아까 만든 서비스의 핵심 비즈니스 로직을 호출!
            missionService.uploadMissionProof(missionId, file);

            // 성공하면 메인 화면으로 이동
            return "redirect:/main";

        } catch (IllegalArgumentException | IOException e) {
            log.error("파일 업로드 중 에러 발생: {}", e.getMessage());

            // 실패하면 에러 메시지를 담아서 다시 업로드 화면을 보여줌
            model.addAttribute("missionId", missionId);
            model.addAttribute("errorMessage", "사진 업로드에 실패했습니다: " + e.getMessage());
            return "mission/proof";
        }
    }
}