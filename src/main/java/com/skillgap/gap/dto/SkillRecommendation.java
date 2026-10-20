package com.skillgap.gap.dto;

import com.skillgap.jobRequirement.model.RequirementType;

public class SkillRecommendation {

    private String skillId;
    private String skillName;

    private String status;
    private RequirementType  requirementType;

    private Integer userProficiency;
    private int requiredProficiency;
    private int proficiencyGap;
    private String recommendationReason;
    private int priority;
    private String impact;
    

    public SkillRecommendation() {
    }

    public SkillRecommendation(
            String skillId,
            String skillName,
            String status,
            RequirementType  requirementType,
            Integer userProficiency,
            int requiredProficiency,
            int proficiencyGap,
            String recommendationReason,
            String impact,
            int priority
    ) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.status = status;
        this.requirementType = requirementType;
        this.userProficiency = userProficiency;
        this.requiredProficiency = requiredProficiency;
        this.proficiencyGap = proficiencyGap;
        this.recommendationReason = recommendationReason;
        this.impact = impact;
        this.priority = priority;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getStatus() {
        return status;
    }

    public RequirementType  getRequirementType() {
        return requirementType;
    }


    public Integer getUserProficiency() {
        return userProficiency;
    }

    public int getRequiredProficiency() {
        return requiredProficiency;
    }
    public int getProficiencyGap() {
    return proficiencyGap;
}
public String getRecommendationReason() {
    return recommendationReason;
}
public String getImpact() {
    return impact;
}

    public int getPriority() {
        return priority;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequirementType(RequirementType  requirementType) {
        this.requirementType = requirementType;
    }

    public void setUserProficiency(Integer userProficiency) {
        this.userProficiency = userProficiency;
    }

    public void setRequiredProficiency(int requiredProficiency) {
        this.requiredProficiency = requiredProficiency;
    }

    public void setProficiencyGap(int proficiencyGap) {
    this.proficiencyGap = proficiencyGap;
}
public void setRecommendationReason(String recommendationReason) {
    this.recommendationReason = recommendationReason;
}
public void setImpact(String impact) {
    this.impact = impact;
}

    public void setPriority(int priority) {
        this.priority = priority;
    }
}