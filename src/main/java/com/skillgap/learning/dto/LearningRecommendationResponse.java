
package com.skillgap.learning.dto;

import java.util.List;

public class LearningRecommendationResponse {

    private String topicId;
    private String topicName;
    private String description;
    private int knowledgeLevel;
    private String status;
    private int priority;
    private String recommendationReason;
    private List<LearningResourceResponse> resources;

    public LearningRecommendationResponse(
            String topicId,
            String topicName,
            String description,
            int knowledgeLevel,
            String status,
            int priority,
            String recommendationReason,
            List<LearningResourceResponse> resources
    ) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.description = description;
        this.knowledgeLevel = knowledgeLevel;
        this.status = status;
        this.priority = priority;
        this.recommendationReason = recommendationReason;
        this.resources = resources;
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

    public int getPriority() {
        return priority;
    }

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public List<LearningResourceResponse> getResources() {
        return resources;
    }
}
