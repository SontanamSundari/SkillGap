
package com.skillgap.learning.controller;

import com.skillgap.learning.dto.AssessmentSessionResponse;
import com.skillgap.learning.dto.CreateAssessmentSessionRequest;
import com.skillgap.learning.dto.SessionAssessmentResultResponse;
import com.skillgap.learning.dto.SubmitSessionAssessmentRequest;
import com.skillgap.learning.service.AssessmentSessionService;
import java.util.List;
import jakarta.validation.Valid;
import com.skillgap.learning.dto.AssessmentHistoryResponse;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning/assessment/sessions")
public class AssessmentSessionController {

    private final AssessmentSessionService assessmentSessionService;

    public AssessmentSessionController(
            AssessmentSessionService assessmentSessionService
    ) {
        this.assessmentSessionService = assessmentSessionService;
    }

    @PostMapping
    public ResponseEntity<AssessmentSessionResponse> createSession(
            Authentication authentication,
            @Valid @RequestBody CreateAssessmentSessionRequest request
    ) {
        String userId = authentication.getName();

        AssessmentSessionResponse response =
                assessmentSessionService.createSession(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<SessionAssessmentResultResponse> submitSession(
            Authentication authentication,
            @Valid @RequestBody SubmitSessionAssessmentRequest request
    ) {
        String userId = authentication.getName();

        SessionAssessmentResultResponse response =
                assessmentSessionService.submitSession(userId, request);

        return ResponseEntity.ok(response);
    }
    
@GetMapping("/history")
public ResponseEntity<List<AssessmentHistoryResponse>> getAssessmentHistory(
        Authentication authentication,
        @RequestParam String jobId
) {
    String userId = authentication.getName();

    List<AssessmentHistoryResponse> history =
            assessmentSessionService.getAssessmentHistory(userId, jobId);

    return ResponseEntity.ok(history);
}

}
