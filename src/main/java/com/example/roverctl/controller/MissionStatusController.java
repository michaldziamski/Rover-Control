package com.example.roverctl.controller;

import com.example.roverctl.dto.response.MissionStatusResponse;
import com.example.roverctl.service.MissionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mission")
@RequiredArgsConstructor
public class MissionStatusController {

    private final MissionStatusService missionStatusService;

    @GetMapping("/status")
    public MissionStatusResponse getStatus() {
        return missionStatusService.getStatus();
    }
}