package com.skillgap.userSkill.repository;

import com.skillgap.userSkill.model.UserSkill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository
        extends MongoRepository<UserSkill, String> {

    List<UserSkill> findByUserId(String userId);

    Optional<UserSkill> findByUserIdAndSkillId(
            String userId,
            String skillId);

    boolean existsByUserIdAndSkillId(
            String userId,
            String skillId);
}