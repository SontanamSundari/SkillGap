
package com.skillgap.learning.model;

import java.util.ArrayList;
import java.util.List;

public class LearningPathItem {

    private String skillId;
    private String skillName;

    private int priority;
    private String gapType;

    private List<String> topics = new ArrayList<>();
    private List<String> completedTopics = new ArrayList<>();
    private List<LearningResource> resources = new ArrayList<>();

    private int estimatedHours;

    private String status = "NOT_STARTED";
    private int progressPercentage;

    public LearningPathItem() {
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getGapType() {
        return gapType;
    }

    public void setGapType(String gapType) {
        this.gapType = gapType;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<LearningResource> getResources() {
        return resources;
    }

    public void setResources(List<LearningResource> resources) {
        this.resources = resources;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    public List<String> getCompletedTopics() {
    return completedTopics;
}

public void setCompletedTopics(List<String> completedTopics) {
    this.completedTopics = completedTopics;
}
}
