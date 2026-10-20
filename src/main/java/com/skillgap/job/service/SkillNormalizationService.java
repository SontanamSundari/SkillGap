package com.skillgap.job.service;

import com.skillgap.job.dto.ExtractedSkill;
import com.skillgap.job.dto.NormalizedSkill;
import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillNormalizationService {
    private final SkillRepository skillRepository;
    public SkillNormalizationService(SkillRepository skillRepository){
        this.skillRepository=skillRepository;
    }

public List<NormalizedSkill> normalizeSkills(List<ExtractedSkill> extractedSkills) {
    List<NormalizedSkill> normalizedSkills = new ArrayList<>();

    if (extractedSkills == null) {
        return normalizedSkills;
    }

    for (ExtractedSkill extractedSkill : extractedSkills) {
        if (extractedSkill == null
                || extractedSkill.getName() == null
                || extractedSkill.getName().isBlank()) {
            continue;
        }

        String skillName = extractedSkill.getName().trim();

        Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                .orElseGet(() -> skillRepository.save(
                        new Skill(skillName, "AI_EXTRACTED")
                ));

        boolean alreadyAdded = normalizedSkills.stream()
                .anyMatch(existingSkill ->
                        existingSkill.getSkillId().equals(skill.getId()));

        if (alreadyAdded) {
            continue;
        }

        normalizedSkills.add(
                new NormalizedSkill(
                        skill.getId(),
                        skill.getName(),
                        extractedSkill.getProficiency(),
                        extractedSkill.getRequirementType()
                )
        );
    }

    return normalizedSkills;
}

    }

