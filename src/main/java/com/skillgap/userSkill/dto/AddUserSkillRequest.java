package com.skillgap.userSkill.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AddUserSkillRequest {

    @NotBlank(message = "Skill ID is required")
    private String skillId;

    @Min(value = 1, message = "Proficiency must be at least 1")
    @Max(value = 5, message = "Proficiency must not exceed 5")
    private int proficiency;

    public AddUserSkillRequest() {
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }
}