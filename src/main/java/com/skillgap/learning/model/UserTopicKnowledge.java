package com.skillgap.learning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userTopicKnowledge")
public class UserTopicKnowledge {

    @Id
    private String id;

    private String userId;

    private String topicId;

    private int knowledgeLevel;

    public UserTopicKnowledge() {
    }

    public UserTopicKnowledge(
            String userId,
            String topicId,
            int knowledgeLevel
    ) {
        this.userId = userId;
        this.topicId = topicId;
        this.knowledgeLevel = knowledgeLevel;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setKnowledgeLevel(int knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }
}