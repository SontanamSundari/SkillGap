package com.skillgap.learning.service;

import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.repository.SkillTopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillTopicService {

    private final SkillTopicRepository skillTopicRepository;

    public SkillTopicService(SkillTopicRepository skillTopicRepository) {
        this.skillTopicRepository = skillTopicRepository;
    }

    public List<SkillTopic> getTopicsBySkillId(String skillId) {

        return skillTopicRepository.findBySkillId(skillId);
    }
}