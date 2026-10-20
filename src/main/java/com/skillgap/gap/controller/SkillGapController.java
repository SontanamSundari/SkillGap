package com.skillgap.gap.controller;
import com.skillgap.gap.dto.SkillGapResult;
import com.skillgap.gap.service.SkillGapService;
import com.skillgap.job.repository.JobRepository;
import com.skillgap.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.skillgap.gap.dto.SkillGapSummary;
import com.skillgap.gap.dto.SkillRecommendation;

import java.util.List;

@RestController 
@RequestMapping("/api/skill-gap")
public class SkillGapController {
        private final JobRepository jobRepository;
        private final SkillGapService skillGapService;

    public SkillGapController(SkillGapService skillGapService, JobRepository jobRepository) {
        this.skillGapService = skillGapService;
        this.jobRepository = jobRepository;
    }


@GetMapping("/jobs/{jobId}")
public ResponseEntity<List<SkillGapResult>> analyzeSkillGap(
        @PathVariable String jobId,
        Authentication authentication) {

    String userId = authentication.getName();

    System.out.println(">>> Skill Gap jobId: " + jobId);
    System.out.println(">>> Authenticated userId: " + userId);

    List<SkillGapResult> results =
            skillGapService.analyzeGap(userId, jobId);

    return ResponseEntity.ok(results);
}

    @GetMapping("/jobs/{jobId}/summary")
    public ResponseEntity<SkillGapSummary> analyzeSkillGapSummary(
            @PathVariable String jobId,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        SkillGapSummary summary =
                skillGapService.analyzeGapSummary(
                        userId,
                        jobId
                );

        return ResponseEntity.ok(summary);
    }
    @GetMapping("/jobs/{jobId}/recommendations")
    public ResponseEntity<List<SkillRecommendation>> getRecommendations(@PathVariable String jobId,Authentication authentication){
        String userId = authentication.getName();
        List<SkillRecommendation> recommendations = skillGapService.getRecommendations(userId, jobId);
        return ResponseEntity.ok(recommendations);
    } 

}
