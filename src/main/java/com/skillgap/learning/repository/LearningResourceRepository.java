package com.skillgap.learning.repository;

import com.skillgap.learning.model.LearningResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LearningResourceRepository
        extends MongoRepository<LearningResource, String> {

    List<LearningResource> findByTopicId(String topicId);
}
