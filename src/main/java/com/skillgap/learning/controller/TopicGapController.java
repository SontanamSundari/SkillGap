package com.skillgap.learning.controller;

import com.skillgap.learning.dto.TopicGapResponse;
import com.skillgap.learning.service.TopicGapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/topic-gap")
public class TopicGapController {

    private final TopicGapService topicGapService;

    public TopicGapController(
            TopicGapService topicGapService
    ) {
        this.topicGapService = topicGapService;
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<TopicGapResponse>> getTopicGap(
            Authentication authentication,
            @PathVariable String skillId
    ) {
        String userId = authentication.getName();

        return ResponseEntity.ok(
                topicGapService.analyzeTopicGap(
                        userId,
                        skillId
                )
        );
    }
}