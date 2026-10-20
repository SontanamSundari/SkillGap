package com.skillgap.learning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skillTopic")
public class SkillTopic {

    @Id
    private String id;

    private String skillId;

    private String name;

    private String description;

    public SkillTopic() {
    }

    public SkillTopic(
            String skillId,
            String name,
            String description
    ) {
        this.skillId = skillId;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}