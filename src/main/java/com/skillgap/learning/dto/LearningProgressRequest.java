
package com.skillgap.learning.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LearningProgressRequest {

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress cannot be below 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progressPercentage;

    public LearningProgressRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
