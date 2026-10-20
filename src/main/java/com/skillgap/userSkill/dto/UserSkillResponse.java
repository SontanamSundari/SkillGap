package com.skillgap.userSkill.dto;

public class UserSkillResponse {

    private String skillId;
    private String skillName;
    private String category;
    private int proficiency;

    public UserSkillResponse() {
    }

    public UserSkillResponse(
            String skillId,
            String skillName,
            String category,
            int proficiency) {

        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiency = proficiency;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getCategory() {
        return category;
    }

    public int getProficiency() {
        return proficiency;
    }
}