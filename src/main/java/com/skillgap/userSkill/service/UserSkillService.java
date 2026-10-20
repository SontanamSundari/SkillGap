package com.skillgap.userSkill.service;

import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;
import com.skillgap.user.repository.UserRepository;
import com.skillgap.userSkill.model.UserSkill;
import com.skillgap.userSkill.repository.UserSkillRepository;
import com.skillgap.userSkill.dto.UserSkillResponse;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.skillgap.common.exception.SkillNotFoundException;
import com.skillgap.common.exception.UserSkillAlreadyExistsException;
import com.skillgap.common.exception.UserSkillNotFoundException;

import java.util.List;

@Service
public class UserSkillService{

    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserSkillService(
            UserSkillRepository userSkillRepository,
            UserRepository userRepository,
            SkillRepository skillRepository) {

        this.userSkillRepository = userSkillRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }


    public UserSkillResponse addSkill(
            String userId,
            String skillId,
            int proficiency) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill not found"));

        if (proficiency < 1 || proficiency > 5) {
            throw new IllegalArgumentException(
                    "Proficiency must be between 1 and 5"
            );
        }

        if (userSkillRepository.existsByUserIdAndSkillId(
                userId, skillId)) {

            throw new UserSkillAlreadyExistsException(
                    "User already has this skill"
            );
        }

        UserSkill userSkill =
                new UserSkill(userId, skillId, proficiency);

        userSkillRepository.save(userSkill);

        return new UserSkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                userSkill.getProficiency()
        );
    }


    public List<UserSkillResponse> getUserSkills(String userId) {

    if (!userRepository.existsById(userId)) {
        throw new RuntimeException("User not found");
    }

    List<UserSkill> userSkills =
            userSkillRepository.findByUserId(userId);

    return userSkills.stream()
            .map(userSkill -> {

                Skill skill = skillRepository
                        .findById(userSkill.getSkillId())
                        .orElseThrow(() ->
                                new RuntimeException("Skill not found"));

                return new UserSkillResponse(
                        skill.getId(),
                        skill.getName(),
                        skill.getCategory(),
                        userSkill.getProficiency()
                );
            })
            .collect(Collectors.toList());
    }


    public UserSkillResponse updateSkill(
            String userId,
            String skillId,
            int proficiency) {

        if (proficiency < 1 || proficiency > 5) {
            throw new IllegalArgumentException(
                    "Proficiency must be between 1 and 5"
            );
        }

        UserSkill userSkill =
                userSkillRepository
                        .findByUserIdAndSkillId(userId, skillId)
                        .orElseThrow(() ->
                                new UserSkillNotFoundException(
                                        "User does not have this skill"
                                ));

        Skill skill = skillRepository
                .findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        userSkill.setProficiency(proficiency);

        userSkillRepository.save(userSkill);

        return new UserSkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory(),
                userSkill.getProficiency()
        );
    }
    public void deleteSkill(String userId,String skillId){
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId).orElseThrow(()->new RuntimeException("User does not have Skill"));
        userSkillRepository.delete(userSkill);
    }
}