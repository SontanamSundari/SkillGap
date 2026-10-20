
package com.skillgap.learning.controller;

import com.skillgap.learning.dto.LearningResourceResponse;
import com.skillgap.learning.service.LearningResourceService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning/resources")
public class LearningResourceController {

    private final LearningResourceService learningResourceService;

    public LearningResourceController(
            LearningResourceService learningResourceService
    ) {
        this.learningResourceService = learningResourceService;
    }

    @GetMapping("/topic/{topicId}")
    public List<LearningResourceResponse> getResourcesByTopicId(
            @PathVariable String topicId,
            Authentication authentication
    ) {
        System.out.println(">>> LearningResourceController reached");

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Authentication required"
            );
        }

        return learningResourceService.getResourcesByTopicId(topicId);
    }
}
