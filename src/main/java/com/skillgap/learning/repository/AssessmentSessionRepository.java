
package com.skillgap.learning.repository;

import com.skillgap.learning.model.AssessmentSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentSessionRepository
        extends MongoRepository<AssessmentSession, String> {

    Optional<AssessmentSession> findByIdAndUserId(
            String id,
            String userId
    );

    List<AssessmentSession> findByUserId(String userId);

    List<AssessmentSession> findByUserIdAndJobId(
            String userId,
            String jobId
    );
}
