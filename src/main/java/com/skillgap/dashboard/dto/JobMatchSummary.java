package com.skillgap.dashboard.dto;

public class JobMatchSummary {

    private String jobId;
    private String title;
    private String company;
    private String location;
    private String employmentType;
    private String experienceLevel;

    private int matchPercentage;
    private int gapScore;
    private String readinessLevel;

    private int matchedSkills;
    private int weakSkills;
    private int missingSkills;
    private int totalRequiredSkills;

    public JobMatchSummary() {
    }

    public JobMatchSummary(
            String jobId,
            String title,
            String company,
            String location,
            String employmentType,
            String experienceLevel,
            int matchPercentage,
            int gapScore,
            String readinessLevel,
            int matchedSkills,
            int weakSkills,
            int missingSkills,
            int totalRequiredSkills
    ) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.matchPercentage = matchPercentage;
        this.gapScore = gapScore;
        this.readinessLevel = readinessLevel;
        this.matchedSkills = matchedSkills;
        this.weakSkills = weakSkills;
        this.missingSkills = missingSkills;
        this.totalRequiredSkills = totalRequiredSkills;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public int getGapScore() {
        return gapScore;
    }

    public String getReadinessLevel() {
        return readinessLevel;
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

    public int getTotalRequiredSkills() {
        return totalRequiredSkills;
    }
}