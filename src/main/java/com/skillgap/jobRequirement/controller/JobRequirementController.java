
package com.skillgap.jobRequirement.controller;

import com.skillgap.jobRequirement.dto.CreateJobRequirementRequest;
import com.skillgap.jobRequirement.model.JobRequirement;
import com.skillgap.jobRequirement.service.JobRequirementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/requirements")
public class JobRequirementController {

    private final JobRequirementService jobRequirementService;

    public JobRequirementController(
            JobRequirementService jobRequirementService) {
        this.jobRequirementService = jobRequirementService;
    }

    @PostMapping
    public ResponseEntity<JobRequirement> createRequirement(
            @PathVariable String jobId,
            @Valid @RequestBody CreateJobRequirementRequest request,
            Authentication authentication) {

        String userId = authentication.getName();

        JobRequirement requirement =
                jobRequirementService.createRequirement(
                        userId,
                        jobId,
                        request.getSkillId(),
                        request.getRequiredProficiency(),
                        request.getRequirementType()
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requirement);
    }

    @GetMapping
    public ResponseEntity<List<JobRequirement>> getRequirements(
            @PathVariable String jobId,
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                jobRequirementService.getRequirementsByJobId(
                        userId, jobId)
        );
    }

    @PostMapping("/ai")
    public ResponseEntity<List<JobRequirement>> generateRequirementsFromAI(
            @PathVariable String jobId,
            Authentication authentication) {

        String userId = authentication.getName();

        List<JobRequirement> requirements =
                jobRequirementService.generateRequirementsFromAI(
                        userId, jobId);

        return ResponseEntity.ok(requirements);
    }
}
