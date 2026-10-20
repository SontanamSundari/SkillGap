
package com.skillgap.learning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "assessmentSession")
public class AssessmentSession {

    @Id
    private String id;

    private String userId;

    private String jobId;

    private List<SessionQuestion> questions = new ArrayList<>();

    private String status;

    private Instant createdAt;

    private Instant submittedAt;

    private Integer scorePercentage;

    public AssessmentSession() {
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getJobId() {
        return jobId;
    }

    public List<SessionQuestion> getQuestions() {
        return questions;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public Integer getScorePercentage() {
        return scorePercentage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setQuestions(List<SessionQuestion> questions) {
        this.questions = questions;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setScorePercentage(Integer scorePercentage) {
        this.scorePercentage = scorePercentage;
    }
}
