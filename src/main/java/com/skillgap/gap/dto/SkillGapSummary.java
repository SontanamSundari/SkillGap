package com.skillgap.gap.dto;

import java.util.List;

public class SkillGapSummary {

    private String jobId;

    private int matchPercentage;

    private int matchedSkills;
    private int weakSkills;
    private int missingSkills;
    private int criticalMissingSkills;
    private int criticalWeakSkills;
    private int totalRequiredSkills;
    private int gapScore;
    private List<SkillGapResult> skills;
    private String readinessLevel;

    public SkillGapSummary() {
    }

public SkillGapSummary(
        String jobId,
        int matchPercentage,
        int gapScore,
        String readinessLevel,
        int matchedSkills,
        int weakSkills,
        int missingSkills,
        int criticalMissingSkills,
        int criticalWeakSkills,
        int totalRequiredSkills,
        List<SkillGapResult> skills
) {
    this.jobId = jobId;
    this.matchPercentage = matchPercentage;
    this.gapScore = gapScore;
    this.readinessLevel = readinessLevel;
    this.matchedSkills = matchedSkills;
    this.weakSkills = weakSkills;
    this.missingSkills = missingSkills;
    this.criticalMissingSkills = criticalMissingSkills;
this.criticalWeakSkills = criticalWeakSkills;
    this.totalRequiredSkills = totalRequiredSkills;
    this.skills = skills;
}

    public String getJobId() {
        return jobId;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public int getMatchedSkills() {
        return matchedSkills;
    }

    public int getWeakSkills() {
        return weakSkills;
    }

    public int getMissingSkills() {
        return missingSkills;
    }

    public int getCriticalMissingSkills() {
    return criticalMissingSkills;
}

public int getCriticalWeakSkills() {
    return criticalWeakSkills;
}

    public int getTotalRequiredSkills() {
        return totalRequiredSkills;
    }
    public String getReadinessLevel() {
    return readinessLevel;
}

    public List<SkillGapResult> getSkills() {
        return skills;
    }
    public int getGapScore() {
    return gapScore;
}

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    public void setReadinessLevel(String readinessLevel) {
    this.readinessLevel = readinessLevel;
}

    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public void setMatchedSkills(int matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public void setWeakSkills(int weakSkills) {
        this.weakSkills = weakSkills;
    }

    public void setMissingSkills(int missingSkills) {
        this.missingSkills = missingSkills;
    }

    public void setCriticalMissingSkills(int criticalMissingSkills) {
    this.criticalMissingSkills = criticalMissingSkills;
}

public void setCriticalWeakSkills(int criticalWeakSkills) {
    this.criticalWeakSkills = criticalWeakSkills;
}
    public void setTotalRequiredSkills(int totalRequiredSkills) {
        this.totalRequiredSkills = totalRequiredSkills;
    }

    public void setSkills(List<SkillGapResult> skills) {
        this.skills = skills;
    }
    public void setGapScore(int gapScore) {
    this.gapScore = gapScore;
}
}