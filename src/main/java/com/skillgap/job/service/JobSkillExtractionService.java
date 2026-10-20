package com.skillgap.job.service;

import com.skillgap.job.dto.ExtractedSkill;
import com.skillgap.job.dto.NormalizedSkill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSkillExtractionService {

    private final SkillExtractionService skillExtractionService;
    private final SkillNormalizationService skillNormalizationService;

    public JobSkillExtractionService(
            SkillExtractionService skillExtractionService,
            SkillNormalizationService skillNormalizationService
    ) {
        this.skillExtractionService = skillExtractionService;
        this.skillNormalizationService = skillNormalizationService;
    }

    public List<NormalizedSkill> extractAndNormalize(
            String jobDescription
    ) {

        List<ExtractedSkill> extractedSkills =
                skillExtractionService.extractSkills(
                        jobDescription
                );

        return skillNormalizationService.normalizeSkills(
                extractedSkills
        );
    }
}