package com.skillgap.userSkill.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_skills")
public class UserSkill {

    @Id
    private String id;

    private String userId;

    private String skillId;

    private int proficiency;

    public UserSkill() {
    }

    public UserSkill(
            String userId,
            String skillId,
            int proficiency) {

        this.userId = userId;
        this.skillId = skillId;
        this.proficiency = proficiency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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