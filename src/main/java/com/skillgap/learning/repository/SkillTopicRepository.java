package com.skillgap.learning.repository;

import com.skillgap.learning.model.SkillTopic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SkillTopicRepository extends MongoRepository<SkillTopic, String> {

    List<SkillTopic> findBySkillId(String skillId);
}