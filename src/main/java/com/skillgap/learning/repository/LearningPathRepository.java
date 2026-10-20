
package com.skillgap.learning.repository;

import com.skillgap.learning.model.LearningPath;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LearningPathRepository
        extends MongoRepository<LearningPath, String> {

    List<LearningPath> findByUserId(String userId);

    Optional<LearningPath> findByUserIdAndJobId(
            String userId,
            String jobId
    );
}
