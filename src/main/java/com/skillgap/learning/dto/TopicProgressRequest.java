
package com.skillgap.learning.dto;

import jakarta.validation.constraints.NotNull;

public class TopicProgressRequest {

    @NotNull(message = "Completion status is required")
    private Boolean completed;

    public TopicProgressRequest() {
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
