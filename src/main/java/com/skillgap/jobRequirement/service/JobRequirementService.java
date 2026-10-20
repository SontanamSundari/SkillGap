
package com.skillgap.jobRequirement.service;

import com.skillgap.common.exception.DuplicateJobRequirementException;
import com.skillgap.common.exception.InvalidRequirementTypeException;
import com.skillgap.common.exception.JobNotFoundException;
import com.skillgap.common.exception.SkillNotFoundException;
import com.skillgap.job.dto.NormalizedSkill;
import com.skillgap.job.repository.JobRepository;
import com.skillgap.job.service.JobSkillExtractionService;
import com.skillgap.jobRequirement.model.JobRequirement;
import com.skillgap.jobRequirement.model.RequirementType;
import com.skillgap.jobRequirement.repository.JobRequirementRepository;
import com.skillgap.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobRequirementService {

    private final JobRequirementRepository jobRequirementRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobSkillExtractionService jobSkillExtractionService;

    public JobRequirementService(
            JobRequirementRepository jobRequirementRepository,
            JobRepository jobRepository,
            SkillRepository skillRepository,
            JobSkillExtractionService jobSkillExtractionService) {
        this.jobRequirementRepository = jobRequirementRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.jobSkillExtractionService = jobSkillExtractionService;
    }

    public JobRequirement createRequirement(
            String userId,
            String jobId,
            String skillId,
            int requiredProficiency,
            RequirementType requirementType) {

        getOwnedJob(jobId, userId);

        skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new SkillNotFoundException(
                                "Skill not found with id: " + skillId));

        if (jobRequirementRepository.existsByJobIdAndSkillId(
                jobId, skillId)) {
            throw new DuplicateJobRequirementException(
                    "This skill is already required for the job");
        }

        JobRequirement requirement = new JobRequirement(
                jobId,
                skillId,
                requiredProficiency,
                requirementType
        );

        return jobRequirementRepository.save(requirement);
    }

    public List<JobRequirement> getRequirementsByJobId(
            String userId, String jobId) {

        getOwnedJob(jobId, userId);

        return jobRequirementRepository.findByJobId(jobId);
    }

    public List<JobRequirement> generateRequirementsFromAI(
            String userId, String jobId) {

        var job = getOwnedJob(jobId, userId);

        List<NormalizedSkill> normalizedSkills =
                jobSkillExtractionService.extractAndNormalize(
                        job.getDescription());

        List<JobRequirement> requirements = new ArrayList<>();

        for (NormalizedSkill skill : normalizedSkills) {

            if (jobRequirementRepository.existsByJobIdAndSkillId(
                    jobId, skill.getSkillId())) {
                continue;
            }

            JobRequirement requirement = new JobRequirement(
                    jobId,
                    skill.getSkillId(),
                    skill.getProficiency(),
                    parseRequirementType(skill.getRequirementType())
            );

            requirements.add(jobRequirementRepository.save(requirement));
        }

        return requirements;
    }

private com.skillgap.job.model.Job getOwnedJob(
        String jobId, String userId) {

    System.out.println(">>> Looking up job ID: " + jobId);
    System.out.println(">>> Looking up owner ID: " + userId);

    var job = jobRepository.findById(jobId);

    System.out.println(">>> Job exists by ID: " + job.isPresent());

    if (job.isPresent()) {
        System.out.println(">>> Stored job owner ID: "
                + job.get().getUserId());
    }

    return jobRepository.findByIdAndUserId(jobId, userId)
            .orElseThrow(() ->
                    new JobNotFoundException(
                            "Job not found with id: " + jobId));
}

    private RequirementType parseRequirementType(
            String requirementType) {

        if (requirementType == null || requirementType.isBlank()) {
            throw new InvalidRequirementTypeException(
                    "Requirement type returned by AI is missing");
        }

        try {
            return RequirementType.valueOf(
                    requirementType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequirementTypeException(
                    "Invalid requirement type returned by AI: "
                            + requirementType);
        }
    }
}
