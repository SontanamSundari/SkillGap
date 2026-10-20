package com.skillgap.learning.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SubmitAssessmentRequest {

    @NotBlank
    private String topicId;

    @NotEmpty
    @Valid
    private List<AssessmentAnswer> answers;

    public SubmitAssessmentRequest() {
    }

    public String getTopicId() {
        return topicId;
    }

    public List<AssessmentAnswer> getAnswers() {
        return answers;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setAnswers(List<AssessmentAnswer> answers) {
        this.answers = answers;
    }
}