package com.skillgap.learning.service;

import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTopicKnowledgeService {

    private final UserTopicKnowledgeRepository repository;

    public UserTopicKnowledgeService(
            UserTopicKnowledgeRepository repository
    ) {
        this.repository = repository;
    }

    public List<UserTopicKnowledge> getUserTopicKnowledge(
            String userId
    ) {
        return repository.findByUserId(userId);
    }
}