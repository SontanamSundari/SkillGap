
package com.skillgap.learning.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SubmitSessionAssessmentRequest {

    @NotBlank
    private String sessionId;

    @NotEmpty
    @Valid
    private List<SessionAnswerRequest> answers;

    public SubmitSessionAssessmentRequest() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<SessionAnswerRequest> getAnswers() {
        return answers;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAnswers(List<SessionAnswerRequest> answers) {
        this.answers = answers;
    }
}
