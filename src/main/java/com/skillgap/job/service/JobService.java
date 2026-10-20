package com.skillgap.job.service;

import com.skillgap.common.exception.JobNotFoundException;
import com.skillgap.job.dto.CreateJobRequest;
import com.skillgap.job.dto.JobResponse;
import com.skillgap.job.model.Job;
import com.skillgap.job.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResponse createJob(String userId, CreateJobRequest request) {

        Job job = new Job(
                userId,
                request.getTitle(),
                request.getCompany(),
                request.getLocation(),
                request.getEmploymentType(),
                request.getExperienceLevel(),
                request.getDescription(),
                Instant.now()
        );

        Job savedJob = jobRepository.save(job);

        return toResponse(savedJob);
    }

    public List<JobResponse> getAllJobs(String userId) {

        return jobRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public JobResponse getJobById(String id, String userId) {

        Job job = jobRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found with id: " + id)
                );

        return toResponse(job);
    }

    private JobResponse toResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getExperienceLevel(),
                job.getDescription(),
                job.getCreatedAt()
        );
    }
}