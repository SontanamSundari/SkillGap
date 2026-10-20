package com.skillgap.learning.dto;

public class TopicGapResponse {

    private String topicId;

    private String topicName;

    private String description;

    private int knowledgeLevel;

    private String status;

    public TopicGapResponse(
            String topicId,
            String topicName,
            String description,
            int knowledgeLevel,
            String status
    ) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.description = description;
        this.knowledgeLevel = knowledgeLevel;
        this.status = status;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getDescription() {
        return description;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public String getStatus() {
        return status;
    }
}