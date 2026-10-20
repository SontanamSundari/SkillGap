package com.skillgap.learning.repository;

import com.skillgap.learning.model.UserTopicKnowledge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserTopicKnowledgeRepository
        extends MongoRepository<UserTopicKnowledge, String> {

    List<UserTopicKnowledge> findByUserId(String userId);
    

    Optional<UserTopicKnowledge> findByUserIdAndTopicId(
            String userId,
            String topicId
    );

    boolean existsByUserIdAndTopicId(
            String userId,
            String topicId
    );
}