package com.skillgap.job.dto;

public class NormalizedSkill {

    private String skillId;
    private String name;
    private int proficiency;
    private String requirementType;

    public NormalizedSkill() {
    }

    public NormalizedSkill(
            String skillId,
            String name,
            int proficiency,
            String requirementType
    ) {
        this.skillId = skillId;
        this.name = name;
        this.proficiency = proficiency;
        this.requirementType = requirementType;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getName() {
        return name;
    }

    public int getProficiency() {
        return proficiency;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }
}