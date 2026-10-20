package com.skillgap.jobRequirement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.skillgap.jobRequirement.model.RequirementType;

@Document(collection = "jobRequirement")
public class JobRequirement {

    @Id
    private String id;

    private String jobId;
    private String skillId;
    private int requiredProficiency;
    private RequirementType requirementType;    

    public JobRequirement() {
    }

    public JobRequirement(
            String jobId,
            String skillId,
            int requiredProficiency,
            RequirementType requirementType
    ) {
        this.jobId = jobId;
        this.skillId = skillId;
        this.requiredProficiency = requiredProficiency;
        this.requirementType = requirementType;
    }

    public String getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    public String getSkillId() {
        return skillId;
    }

    public int getRequiredProficiency() {
        return requiredProficiency;
    }

    public RequirementType getRequirementType() {
        return requirementType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setRequiredProficiency(int requiredProficiency) {
        this.requiredProficiency = requiredProficiency;
    }

    public void setRequirementType(RequirementType requirementType) {
        this.requirementType = requirementType;
    }
}