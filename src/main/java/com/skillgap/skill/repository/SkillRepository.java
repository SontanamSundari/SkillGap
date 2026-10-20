package com.skillgap.skill.repository;

import com.skillgap.skill.model.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {
    Optional<Skill> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}