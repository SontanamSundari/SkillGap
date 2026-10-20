package com.skillgap.learning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "learningResource")
public class LearningResource {

    @Id
    private String id;

    private String topicId;

    private String title;

    private String resourceType;

    private String url;

    private String difficulty;

    public LearningResource() {
    }

    public LearningResource(
            String topicId,
            String title,
            String resourceType,
            String url,
            String difficulty
    ) {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}