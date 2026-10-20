
package com.skillgap.learning.dto;

public class LearningProgressSummary {

    private int totalSkills;
    private int completedSkills;
    private int inProgressSkills;
    private int notStartedSkills;
    private double overallProgressPercentage;
    private boolean learningPathCompleted;
    public LearningProgressSummary() {
    }

    public int getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(int totalSkills) {
        this.totalSkills = totalSkills;
    }

    public int getCompletedSkills() {
        return completedSkills;
    }

    public void setCompletedSkills(int completedSkills) {
        this.completedSkills = completedSkills;
    }

    public int getInProgressSkills() {
        return inProgressSkills;
    }

    public void setInProgressSkills(int inProgressSkills) {
        this.inProgressSkills = inProgressSkills;
    }

    public int getNotStartedSkills() {
        return notStartedSkills;
    }

    public void setNotStartedSkills(int notStartedSkills) {
        this.notStartedSkills = notStartedSkills;
    }

    public double getOverallProgressPercentage() {
        return overallProgressPercentage;
    }

    public void setOverallProgressPercentage(double overallProgressPercentage) {
        this.overallProgressPercentage = overallProgressPercentage;
    }
    public boolean isLearningPathCompleted() {
    return learningPathCompleted;
}

public void setLearningPathCompleted(boolean learningPathCompleted) {
    this.learningPathCompleted = learningPathCompleted;
}
}
