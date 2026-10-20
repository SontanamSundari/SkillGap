
package com.skillgap.learning.service;

import com.skillgap.learning.dto.LearningRecommendationResponse;
import com.skillgap.learning.dto.LearningResourceResponse;
import com.skillgap.learning.dto.TopicGapResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LearningRecommendationService {

    private final TopicGapService topicGapService;
    private final LearningResourceService learningResourceService;

    public LearningRecommendationService(
            TopicGapService topicGapService,
            LearningResourceService learningResourceService
    ) {
        this.topicGapService = topicGapService;
        this.learningResourceService = learningResourceService;
    }

    public List<LearningRecommendationResponse> getRecommendations(
            String userId,
            String skillId
    ) {

        List<TopicGapResponse> topicGaps =
                topicGapService.analyzeTopicGap(userId, skillId);

        return topicGaps.stream()
                .filter(topic -> !topic.getStatus().equals("KNOWN"))
                .map(topic -> {

                    int priority =
                            topic.getStatus().equals("UNKNOWN") ? 1 : 2;

                    String reason =
                            topic.getStatus().equals("UNKNOWN")
                                    ? "You have not demonstrated knowledge of this topic yet."
                                    : "Your current knowledge is partial. Strengthen this topic.";

                    List<LearningResourceResponse> resources =
                            learningResourceService.getResourcesByTopicId(
                                    topic.getTopicId()
                            );

                    return new LearningRecommendationResponse(
                            topic.getTopicId(),
                            topic.getTopicName(),
                            topic.getDescription(),
                            topic.getKnowledgeLevel(),
                            topic.getStatus(),
                            priority,
                            reason,
                            resources
                    );
                })
                .sorted(
                        Comparator.comparingInt(
                                LearningRecommendationResponse::getPriority
                        )
                )
                .toList();
    }
}
