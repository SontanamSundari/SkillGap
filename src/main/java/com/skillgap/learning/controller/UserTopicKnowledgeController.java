
package com.skillgap.learning.controller;

import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
public class UserTopicKnowledgeController {

    private final UserTopicKnowledgeRepository knowledgeRepository;

    public UserTopicKnowledgeController(
            UserTopicKnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    @GetMapping("/topic-knowledge")
    public List<UserTopicKnowledge> getMyTopicKnowledge(
            Authentication authentication) {

        String userId = authentication.getName();

        return knowledgeRepository.findByUserId(userId);
    }
}
