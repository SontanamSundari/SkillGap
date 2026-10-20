
package com.skillgap.learning.controller;

import com.skillgap.learning.model.LearningPath;
import com.skillgap.learning.repository.LearningPathRepository;
import com.skillgap.learning.service.LearningPathService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.skillgap.learning.dto.LearningProgressRequest;
import com.skillgap.learning.model.LearningPath;
import jakarta.validation.Valid;
import com.skillgap.learning.dto.LearningProgressSummary;
import com.skillgap.learning.dto.TopicProgressRequest;
import org.springframework.web.bind.annotation.RequestParam;
import com.skillgap.learning.dto.TopicProgressRequest;

@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathController {

    private final LearningPathService learningPathService;
    private final LearningPathRepository learningPathRepository;

    public LearningPathController(
            LearningPathService learningPathService,
            LearningPathRepository learningPathRepository
    ) {
        this.learningPathService = learningPathService;
        this.learningPathRepository = learningPathRepository;
    }

    @PostMapping("/jobs/{jobId}/generate")
    public LearningPath generateLearningPath(
            @PathVariable String jobId,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return learningPathService.generateLearningPath(
                userId,
                jobId
        );
    }

    @GetMapping("/jobs/{jobId}")
    public LearningPath getLearningPath(
            @PathVariable String jobId,
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return learningPathRepository
                .findByUserIdAndJobId(userId, jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Learning path not found for this job"
                        )
                );
    }

    @GetMapping
    public List<LearningPath> getMyLearningPaths(
            Authentication authentication
    ) {
        String userId = authentication.getName();

        return learningPathRepository.findByUserId(userId);
    }
    
@PatchMapping("/jobs/{jobId}/skills/{skillId}/progress")
public LearningPath updateSkillProgress(
        @PathVariable String jobId,
        @PathVariable String skillId,
        @Valid @RequestBody LearningProgressRequest request,
        Authentication authentication) {

    String userId = authentication.getName();

    return learningPathService.updateSkillProgress(
            userId,
            jobId,
            skillId,
            request
    );
}


@GetMapping("/jobs/{jobId}/progress-summary")
public LearningProgressSummary getProgressSummary(
        @PathVariable String jobId,
        Authentication authentication) {

    String userId = authentication.getName();

    return learningPathService.getProgressSummary(
            userId,
            jobId
    );
}


@PatchMapping("/jobs/{jobId}/skills/{skillId}/topics/progress")
public LearningPath updateTopicProgress(
        @PathVariable String jobId,
        @PathVariable String skillId,
        @RequestParam String topicName,
        @Valid @RequestBody TopicProgressRequest request,
        Authentication authentication) {

    String userId = authentication.getName();

    return learningPathService.updateTopicProgress(
            userId,
            jobId,
            skillId,
            topicName,
            request
    );
}

}
