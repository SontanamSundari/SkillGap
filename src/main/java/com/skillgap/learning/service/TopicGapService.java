package com.skillgap.learning.service;

import com.skillgap.learning.dto.TopicGapResponse;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.SkillTopicRepository;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicGapService {

    private final SkillTopicRepository skillTopicRepository;
    private final UserTopicKnowledgeRepository knowledgeRepository;

    public TopicGapService(
            SkillTopicRepository skillTopicRepository,
            UserTopicKnowledgeRepository knowledgeRepository
    ) {
        this.skillTopicRepository = skillTopicRepository;
        this.knowledgeRepository = knowledgeRepository;
    }

    public List<TopicGapResponse> analyzeTopicGap(
            String userId,
            String skillId
    ) {
        List<SkillTopic> topics =
                skillTopicRepository.findBySkillId(skillId);

        List<TopicGapResponse> results = new ArrayList<>();

        for (SkillTopic topic : topics) {

            UserTopicKnowledge knowledge =
                    knowledgeRepository
                            .findByUserIdAndTopicId(
                                    userId,
                                    topic.getId()
                            )
                            .orElse(null);

            int knowledgeLevel = 0;

            if (knowledge != null) {
                knowledgeLevel = knowledge.getKnowledgeLevel();
            }

            String status = determineStatus(knowledgeLevel);

            results.add(
                    new TopicGapResponse(
                            topic.getId(),
                            topic.getName(),
                            topic.getDescription(),
                            knowledgeLevel,
                            status
                    )
            );
        }

        return results;
    }

    private String determineStatus(int knowledgeLevel) {

        if (knowledgeLevel >= 5) {
            return "KNOWN";
        }

        if (knowledgeLevel >= 3) {
            return "WEAK";
        }

        return "UNKNOWN";
    }
}