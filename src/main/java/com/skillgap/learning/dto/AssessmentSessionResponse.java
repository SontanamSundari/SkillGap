
package com.skillgap.learning.dto;

import java.time.Instant;
import java.util.List;

public class AssessmentSessionResponse {

    private String sessionId;
    private String jobId;
    private String status;
    private Instant createdAt;
    private List<SessionQuestionResponse> questions;

    public AssessmentSessionResponse() {
    }

    public AssessmentSessionResponse(
            String sessionId,
            String jobId,
            String status,
            Instant createdAt,
            List<SessionQuestionResponse> questions
    ) {
        this.sessionId = sessionId;
        this.jobId = jobId;
        this.status = status;
        this.createdAt = createdAt;
        this.questions = questions;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<SessionQuestionResponse> getQuestions() {
        return questions;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setQuestions(List<SessionQuestionResponse> questions) {
        this.questions = questions;
    }
}
