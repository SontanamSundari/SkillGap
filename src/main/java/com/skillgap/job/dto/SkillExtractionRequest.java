package com.skillgap.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SkillExtractionRequest {

    @NotBlank(message = "Job description is required")
    @Size(min = 50, max = 10000,
            message = "Job description must be between 50 and 10000 characters")
    private String jobDescription;

    public SkillExtractionRequest() {
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}