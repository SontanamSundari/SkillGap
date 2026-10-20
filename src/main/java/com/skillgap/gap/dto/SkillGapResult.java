package com.skillgap.gap.dto;

import com.skillgap.jobRequirement.model.RequirementType;

public class SkillGapResult {

    private String skillId;
    private String skillName;

    private Integer userProficiency;
    private int requiredProficiency;

    private String status;
    private RequirementType  requirementType;

    public SkillGapResult() {
    }

    public SkillGapResult(
            String skillId,
            String skillName,
            Integer userProficiency,
            int requiredProficiency,
            String status,
            RequirementType  requirementType
    ) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.userProficiency = userProficiency;
        this.requiredProficiency = requiredProficiency;
        this.status = status;
        this.requirementType = requirementType;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public Integer getUserProficiency() {
        return userProficiency;
    }

    public int getRequiredProficiency() {
        return requiredProficiency;
    }

    public String getStatus() {
        return status;
    }

    public RequirementType  getRequirementType() {
        return requirementType;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setUserProficiency(Integer userProficiency) {
        this.userProficiency = userProficiency;
    }

    public void setRequiredProficiency(int requiredProficiency) {
        this.requiredProficiency = requiredProficiency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequirementType(RequirementType  requirementType) {
        this.requirementType = requirementType;
    }
}