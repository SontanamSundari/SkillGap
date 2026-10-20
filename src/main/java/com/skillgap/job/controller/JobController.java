package com.skillgap.job.controller;

import com.skillgap.job.dto.CreateJobRequest;
import com.skillgap.job.dto.JobResponse;
import com.skillgap.job.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        JobResponse response = jobService.createJob(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(jobService.getAllJobs(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable String id,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(jobService.getJobById(id, userId));
    }
}