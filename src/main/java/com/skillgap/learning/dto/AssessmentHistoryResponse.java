
package com.skillgap.learning.dto;

import java.time.Instant;

public class AssessmentHistoryResponse {

    private String sessionId;
    private String jobId;
    private String status;
    private Instant createdAt;
    private Instant submittedAt;
    private Integer scorePercentage;
    private Integer totalQuestions;

    public AssessmentHistoryResponse() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Integer getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(Integer scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
