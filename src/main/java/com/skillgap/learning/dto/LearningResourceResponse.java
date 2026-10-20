
package com.skillgap.learning.dto;

public class LearningResourceResponse {

    private String id;
    private String topicId;
    private String title;
    private String resourceType;
    private String url;
    private String difficulty;

    public LearningResourceResponse() {
    }

    public LearningResourceResponse(
            String id,
            String topicId,
            String title,
            String resourceType,
            String url,
            String difficulty
    ) {
        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.resourceType = resourceType;
        this.url = url;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTitle() {
        return title;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getUrl() {
        return url;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
