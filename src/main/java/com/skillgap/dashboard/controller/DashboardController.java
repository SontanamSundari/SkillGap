package com.skillgap.dashboard.controller;

import com.skillgap.dashboard.dto.JobMatchSummary;
import com.skillgap.dashboard.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/job-matches")
    public ResponseEntity<List<JobMatchSummary>> getJobMatches(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(
                dashboardService.getJobMatches(userId)
        );
    }
}