package com.skillgap.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @NotBlank(message = "Company is required")
    @Size(max = 150, message = "Company must not exceed 150 characters")
    private String company;

    @NotBlank(message = "Location is required")
    @Size(max = 150, message = "Location must not exceed 150 characters")
    private String location;

    @NotBlank(message = "Employment type is required")
    @Size(max = 50, message = "Employment type must not exceed 50 characters")
    private String employmentType;

    @NotBlank(message = "Experience level is required")
    @Size(max = 50, message = "Experience level must not exceed 50 characters")
    private String experienceLevel;

    @NotBlank(message = "Description is required")
    @Size(min = 50, max = 10000, message = "Description must be between 50 and 10000 characters")
    private String description;

    public CreateJobRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}