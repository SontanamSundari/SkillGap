package com.skillgap.job.repository;

import com.skillgap.job.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, String> {

    List<Job> findByUserId(String userId);

    Optional<Job> findByIdAndUserId(String id, String userId);
}