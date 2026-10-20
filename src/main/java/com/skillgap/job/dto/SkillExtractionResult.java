package com.skillgap.job.dto;

import java.util.List;

public class SkillExtractionResult {

    private List<ExtractedSkill> skills;

    public SkillExtractionResult() {
    }

    public List<ExtractedSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<ExtractedSkill> skills) {
        this.skills = skills;
    }
}