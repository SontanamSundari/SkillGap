package com.skillgap.learning.repository;

import com.skillgap.learning.model.AssessmentQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssessmentQuestionRepository
        extends MongoRepository<AssessmentQuestion, String> {

    List<AssessmentQuestion> findByTopicId(String topicId);
}