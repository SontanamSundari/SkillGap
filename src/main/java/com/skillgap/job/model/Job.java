package com.skillgap.job.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "jobs")
public class Job {

    @Id
    private String id;

    private String userId;

    private String title;

    private String company;

    private String location;

    private String employmentType;

    private String experienceLevel;

    private String description;

    private Instant createdAt;

    public Job() {
    }

    public Job(
            String userId,
            String title,
            String company,
            String location,
            String employmentType,
            String experienceLevel,
            String description,
            Instant createdAt
    ) {
        this.userId = userId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.experienceLevel = experienceLevel;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
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

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}