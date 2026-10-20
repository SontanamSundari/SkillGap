package com.skillgap.learning.controller;

import com.skillgap.learning.dto.LearningRecommendationResponse;
import com.skillgap.learning.service.LearningRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/recommendations")
public class LearningRecommendationController {

    private final LearningRecommendationService learningRecommendationService;

    public LearningRecommendationController(
            LearningRecommendationService learningRecommendationService
    ) {
        this.learningRecommendationService =
                learningRecommendationService;
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<LearningRecommendationResponse>>
    getRecommendations(
            Authentication authentication,
            @PathVariable String skillId
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(
                learningRecommendationService.getRecommendations(
                        userId,
                        skillId
                )
        );
    }
}