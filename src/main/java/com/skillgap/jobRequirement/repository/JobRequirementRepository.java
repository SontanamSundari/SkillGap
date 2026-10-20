package com.skillgap.jobRequirement.repository;

import com.skillgap.jobRequirement.model.JobRequirement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobRequirementRepository extends MongoRepository<JobRequirement, String> {

    List<JobRequirement> findByJobId(String jobId);

    boolean existsByJobIdAndSkillId(String jobId, String skillId);
}