package com.skillgap.job.dto;

public class ExtractedSkill {

    private String name;
    private int proficiency;
    private String requirementType;

    public ExtractedSkill() {
    }

    public ExtractedSkill(
            String name,
            int proficiency,
            String requirementType
    ) {
        this.name = name;
        this.proficiency = proficiency;
        this.requirementType = requirementType;
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