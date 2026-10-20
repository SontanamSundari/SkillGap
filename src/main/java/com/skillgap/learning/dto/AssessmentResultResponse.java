package com.skillgap.learning.dto;

public class AssessmentResultResponse {

    private String topicId;

    private int correctAnswers;

    private int totalQuestions;

    private int scorePercentage;

    private int knowledgeLevel;

    public AssessmentResultResponse(
            String topicId,
            int correctAnswers,
            int totalQuestions,
            int scorePercentage,
            int knowledgeLevel
    ) {
        this.topicId = topicId;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.scorePercentage = scorePercentage;
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getTopicId() {
        return topicId;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getScorePercentage() {
        return scorePercentage;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }
}