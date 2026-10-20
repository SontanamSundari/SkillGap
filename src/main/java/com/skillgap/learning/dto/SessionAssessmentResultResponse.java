
package com.skillgap.learning.dto;

import java.time.Instant;
import java.util.List;

public class SessionAssessmentResultResponse {

    private String sessionId;
    private String jobId;
    private String status;
    private int totalQuestions;
    private int correctAnswers;
    private int scorePercentage;
    private Instant submittedAt;
    private List<TopicAssessmentResult> topicResults;

    public SessionAssessmentResultResponse() {
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(int scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public List<TopicAssessmentResult> getTopicResults() {
        return topicResults;
    }

    public void setTopicResults(List<TopicAssessmentResult> topicResults) {
        this.topicResults = topicResults;
    }

    public static class TopicAssessmentResult {

        private String skillId;
        private String topicId;
        private int totalQuestions;
        private int correctAnswers;
        private int knowledgeLevel;

        public TopicAssessmentResult() {
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public void setCorrectAnswers(int correctAnswers) {
            this.correctAnswers = correctAnswers;
        }

        public int getKnowledgeLevel() {
            return knowledgeLevel;
        }

        public void setKnowledgeLevel(int knowledgeLevel) {
            this.knowledgeLevel = knowledgeLevel;
        }
    }
}
