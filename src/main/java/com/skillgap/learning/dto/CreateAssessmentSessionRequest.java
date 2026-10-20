
package com.skillgap.learning.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAssessmentSessionRequest {

    @NotBlank(message = "Job ID is required")
    private String jobId;

    // Optional: provided only for an individual skill assessment
    private String skillId;

    public CreateAssessmentSessionRequest() {
    }

    public CreateAssessmentSessionRequest(String jobId) {
        this.jobId = jobId;
    }

    public CreateAssessmentSessionRequest(String jobId, String skillId) {
        this.jobId = jobId;
        this.skillId = skillId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
}
