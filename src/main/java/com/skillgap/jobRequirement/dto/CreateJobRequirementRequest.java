package com.skillgap.jobRequirement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.skillgap.jobRequirement.model.RequirementType;

public class CreateJobRequirementRequest {

    @NotBlank(message = "Skill ID is required")
    private String skillId;

    @NotNull(message = "Required proficiency is required")
    @Min(value = 1, message = "Required proficiency must be between 1 and 5")
    @Max(value = 5, message = "Required proficiency must be between 1 and 5")
    private Integer requiredProficiency;

    // @NotBlank(message = "Requirement type is required")
    @NotNull(message = "Requirement type is required")
    private RequirementType requirementType;

    public CreateJobRequirementRequest() {
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public Integer getRequiredProficiency() {
        return requiredProficiency;
    }

    public void setRequiredProficiency(Integer requiredProficiency) {
        this.requiredProficiency = requiredProficiency;
    }

    public RequirementType getRequirementType() {
       return requirementType;
    }

    public void setRequirementType(RequirementType requirementType) {
        this.requirementType = requirementType;
    }
}