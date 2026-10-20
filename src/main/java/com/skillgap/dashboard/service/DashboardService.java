package com.skillgap.dashboard.service;

import com.skillgap.dashboard.dto.JobMatchSummary;
import com.skillgap.gap.dto.SkillGapSummary;
import com.skillgap.gap.service.SkillGapService;
import com.skillgap.job.model.Job;
import com.skillgap.job.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final JobRepository jobRepository;
    private final SkillGapService skillGapService;

    public DashboardService(
            JobRepository jobRepository,
            SkillGapService skillGapService
    ) {
        this.jobRepository = jobRepository;
        this.skillGapService = skillGapService;
    }

    public List<JobMatchSummary> getJobMatches(String userId) {

        List<Job> jobs = jobRepository.findByUserId(userId);

        List<JobMatchSummary> results = new ArrayList<>();

        for (Job job : jobs) {

            SkillGapSummary summary =
                    skillGapService.analyzeGapSummary(
                            userId,
                            job.getId()
                    );

            JobMatchSummary matchSummary =
                    new JobMatchSummary(
                            job.getId(),
                            job.getTitle(),
                            job.getCompany(),
                            job.getLocation(),
                            job.getEmploymentType(),
                            job.getExperienceLevel(),
                            summary.getMatchPercentage(),
                            summary.getGapScore(),
                            summary.getReadinessLevel(),
                            summary.getMatchedSkills(),
                            summary.getWeakSkills(),
                            summary.getMissingSkills(),
                            summary.getTotalRequiredSkills()
                    );

            results.add(matchSummary);
        }

        return results;
    }
}