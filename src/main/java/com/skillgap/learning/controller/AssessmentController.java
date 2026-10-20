
package com.skillgap.learning.controller;

import com.skillgap.learning.dto.AssessmentResultResponse;
import com.skillgap.learning.dto.SubmitAssessmentRequest;
import com.skillgap.learning.service.AssessmentQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning/assessment")
public class AssessmentController {

    private final AssessmentQuestionService assessmentQuestionService;

    public AssessmentController(
            AssessmentQuestionService assessmentQuestionService) {
        this.assessmentQuestionService = assessmentQuestionService;
    }

    @PostMapping("/submit")
    public ResponseEntity<AssessmentResultResponse> submitAssessment(
            Authentication authentication,
            @Valid @RequestBody SubmitAssessmentRequest request) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Authentication required");
        }

        String userId = authentication.getName();

        return ResponseEntity.ok(
                assessmentQuestionService.submitAssessment(
                        userId,
                        request));
    }
}
