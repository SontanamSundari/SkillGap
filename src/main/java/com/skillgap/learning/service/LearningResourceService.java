
package com.skillgap.learning.service;

import com.skillgap.learning.dto.LearningResourceResponse;
import com.skillgap.learning.model.LearningResource;
import com.skillgap.learning.repository.LearningResourceRepository;
import com.skillgap.learning.repository.SkillTopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningResourceService {

    private final LearningResourceRepository learningResourceRepository;
    private final SkillTopicRepository skillTopicRepository;

    public LearningResourceService(
            LearningResourceRepository learningResourceRepository,
            SkillTopicRepository skillTopicRepository
    ) {
        this.learningResourceRepository = learningResourceRepository;
        this.skillTopicRepository = skillTopicRepository;
    }

    public List<LearningResourceResponse> getResourcesByTopicId(String topicId) {

        boolean topicExists = skillTopicRepository.existsById(topicId);

        if (!topicExists) {
            throw new RuntimeException("Topic not found: " + topicId);
        }

        List<LearningResource> resources =
                learningResourceRepository.findByTopicId(topicId);

        return resources.stream()
                .map(resource -> new LearningResourceResponse(
                        resource.getId(),
                        resource.getTopicId(),
                        resource.getTitle(),
                        resource.getResourceType(),
                        resource.getUrl(),
                        resource.getDifficulty()
                ))
                .toList();
    }
}
