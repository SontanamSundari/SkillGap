package com.skillgap.userSkill.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateUserSkillRequest {

    @Min(value = 1, message = "Proficiency must be at least 1")
    @Max(value = 5, message = "Proficiency must not exceed 5")
    private int proficiency;

    public UpdateUserSkillRequest() {
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }
}