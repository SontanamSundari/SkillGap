package com.skillgap.learning.service;

import com.skillgap.gap.dto.SkillRecommendation;
import com.skillgap.gap.service.SkillGapService;
import com.skillgap.learning.model.LearningPath;
import com.skillgap.learning.model.LearningPathItem;
import com.skillgap.learning.model.LearningResource;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.repository.LearningPathRepository;
import com.skillgap.learning.repository.LearningResourceRepository;
import com.skillgap.learning.repository.SkillTopicRepository;
import com.skillgap.learning.dto.TopicProgressRequest;
import org.springframework.stereotype.Service;
import com.skillgap.jobRequirement.model.JobRequirement;
import com.skillgap.jobRequirement.repository.JobRequirementRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.skillgap.gap.dto.SkillGapResult;
import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.skillgap.learning.dto.LearningProgressRequest;
import com.skillgap.learning.model.LearningPathItem;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.skillgap.learning.dto.LearningProgressSummary;

@Service
public class LearningPathService {
    
    private final SkillGapService skillGapService;
    private final LearningPathRepository learningPathRepository;
    private final SkillTopicRepository skillTopicRepository;
    private final LearningResourceRepository learningResourceRepository;
    private final JobRequirementRepository jobRequirementRepository;
    private final SkillRepository skillRepository;
    
       public LearningPathService(
            SkillGapService skillGapService,
            LearningPathRepository learningPathRepository,
            SkillTopicRepository skillTopicRepository,
            LearningResourceRepository learningResourceRepository,
            JobRequirementRepository jobRequirementRepository,
            SkillRepository skillRepository
    ) {
        this.skillGapService = skillGapService;
        this.learningPathRepository = learningPathRepository;
        this.skillTopicRepository = skillTopicRepository;
        this.learningResourceRepository = learningResourceRepository;
        this.jobRequirementRepository = jobRequirementRepository;
        this.skillRepository = skillRepository;
    }



public LearningPath generateLearningPath(String userId, String jobId) {

    List<SkillGapResult> gapResults =
            skillGapService.analyzeGap(userId, jobId);

    Map<String, SkillGapResult> gapMap = new HashMap<>();
    for (SkillGapResult result : gapResults) {
        gapMap.put(result.getSkillId(), result);
    }

    List<SkillRecommendation> recommendations =
            skillGapService.getRecommendations(userId, jobId);

    Map<String, SkillRecommendation> recommendationMap = new HashMap<>();
    for (SkillRecommendation recommendation : recommendations) {
        recommendationMap.put(recommendation.getSkillId(), recommendation);
    }

    List<JobRequirement> requirements =
            jobRequirementRepository.findByJobId(jobId);

    LearningPath learningPath = learningPathRepository
            .findByUserIdAndJobId(userId, jobId)
            .orElseGet(LearningPath::new);

    Map<String, LearningPathItem> previousItems = new HashMap<>();

    if (learningPath.getItems() != null) {
        for (LearningPathItem previous : learningPath.getItems()) {
            previousItems.put(previous.getSkillId(), previous);
        }
    }

    List<LearningPathItem> learningItems = new ArrayList<>();

    for (JobRequirement requirement : requirements) {

        String skillId = requirement.getSkillId();

        // Prevent duplicate requirements from creating duplicate cards.
        if (learningItems.stream()
                .anyMatch(existing -> skillId.equals(existing.getSkillId()))) {
            continue;
        }

        SkillGapResult gap = gapMap.get(skillId);
        SkillRecommendation recommendation = recommendationMap.get(skillId);

        Skill skill = skillRepository.findById(skillId).orElse(null);

        // A requirement without a valid skill record cannot be displayed correctly.
        if (skill == null) {
            continue;
        }

        LearningPathItem item = new LearningPathItem();
        item.setSkillId(skillId);
        item.setSkillName(skill.getName());

        if (gap != null) {
            item.setGapType(gap.getStatus());
        } else {
            item.setGapType("UNKNOWN");
        }

        if (recommendation != null) {
            item.setPriority(recommendation.getPriority());
        } else if (gap != null && "MATCHED".equals(gap.getStatus())) {
            item.setPriority(5);
        } else {
            item.setPriority(4);
        }

        // Preserve topic order while removing duplicate topic names.
        Set<String> uniqueTopicNames = new LinkedHashSet<>();
        List<LearningResource> resources = new ArrayList<>();
        Set<String> resourceKeys = new LinkedHashSet<>();

        List<SkillTopic> topics =
                skillTopicRepository.findBySkillId(skillId);

        for (SkillTopic topic : topics) {
            if (topic.getName() == null || topic.getName().isBlank()) {
                continue;
            }

            if (uniqueTopicNames.add(topic.getName())) {
                List<LearningResource> topicResources =
                        learningResourceRepository.findByTopicId(topic.getId());

                for (LearningResource resource : topicResources) {
                    String key = resource.getUrl() != null
                            ? resource.getUrl().trim()
                            : resource.getTitle() != null
                                    ? resource.getTitle().trim()
                                    : null;

                    if (key != null && resourceKeys.add(key)) {
                        resources.add(resource);
                    }
                }
            }
        }

        List<String> topicNames = new ArrayList<>(uniqueTopicNames);
        item.setTopics(topicNames);
        item.setResources(resources);
        item.setEstimatedHours(topicNames.size() * 2);

        LearningPathItem previous = previousItems.get(skillId);

        if (previous != null) {
            List<String> completedTopics =
                    previous.getCompletedTopics() != null
                            ? new ArrayList<>(previous.getCompletedTopics())
                            : new ArrayList<>();

            completedTopics.retainAll(topicNames);
            item.setCompletedTopics(completedTopics);

            int totalTopics = topicNames.size();
            int completedCount = completedTopics.size();
            int progress = totalTopics == 0
                    ? 0
                    : (completedCount * 100) / totalTopics;

            item.setProgressPercentage(progress);

            if (progress == 100 && totalTopics > 0) {
                item.setStatus("COMPLETED");
            } else if (progress > 0) {
                item.setStatus("IN_PROGRESS");
            } else {
                item.setStatus("NOT_STARTED");
            }
        } else {
            item.setCompletedTopics(new ArrayList<>());
            item.setStatus("NOT_STARTED");
            item.setProgressPercentage(0);
        }

        learningItems.add(item);
    }

    learningPath.setUserId(userId);
    learningPath.setJobId(jobId);
    learningPath.setItems(learningItems);
    learningPath.setStatus("ACTIVE");

    Instant now = Instant.now();

    if (learningPath.getCreatedAt() == null) {
        learningPath.setCreatedAt(now);
    }

    learningPath.setUpdatedAt(now);

    return learningPathRepository.save(learningPath);
}



public LearningPath updateSkillProgress(
        String userId,
        String jobId,
        String skillId,
        LearningProgressRequest request) {

    if (request == null || request.getStatus() == null ||
            !java.util.Set.of("NOT_STARTED", "IN_PROGRESS", "COMPLETED")
                    .contains(request.getStatus())) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid status. Allowed: NOT_STARTED, IN_PROGRESS, COMPLETED"
        );
    }

    Integer progress = request.getProgressPercentage();

    if (progress == null || progress < 0 || progress > 100) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Progress percentage must be between 0 and 100"
        );
    }

    if ("COMPLETED".equals(request.getStatus()) && progress != 100) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A completed skill must have 100% progress"
        );
    }

    if ("NOT_STARTED".equals(request.getStatus()) && progress != 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A not-started skill must have 0% progress"
        );
    }

    LearningPath path = learningPathRepository
            .findByUserIdAndJobId(userId, jobId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Learning path not found for this job"
            ));

    LearningPathItem item = path.getItems().stream()
            .filter(existing -> skillId.equals(existing.getSkillId()))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Skill not found in this learning path"
            ));

    // Topic completion is the source of truth when topics exist.
    if (item.getTopics() != null && !item.getTopics().isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Update this skill through topic progress instead."
        );
    }

    item.setStatus(request.getStatus());
    item.setProgressPercentage(progress);
    path.setUpdatedAt(java.time.Instant.now());

    return learningPathRepository.save(path);
}


public LearningProgressSummary getProgressSummary(
        String userId,
        String jobId) {

    LearningPath path = learningPathRepository
            .findByUserIdAndJobId(userId, jobId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Learning path not found for this job"
            ));

    LearningProgressSummary summary =
            new LearningProgressSummary();

    int totalSkills = path.getItems().size();
    int completed = 0;
    int inProgress = 0;
    int notStarted = 0;
    int totalProgress = 0;

    for (LearningPathItem item : path.getItems()) {

        String status = item.getStatus();

        if ("COMPLETED".equals(status)) {
            completed++;
        } else if ("IN_PROGRESS".equals(status)) {
            inProgress++;
        } else if ("NOT_STARTED".equals(status)) {
            notStarted++;
        }

        Integer progress = item.getProgressPercentage();

        if (progress != null) {
            totalProgress += progress;
        }
    }

    double overallProgress = totalSkills == 0
            ? 0.0
            : Math.round(
                    (totalProgress * 100.0 / totalSkills)
            ) / 100.0;

    summary.setTotalSkills(totalSkills);
    summary.setCompletedSkills(completed);
    summary.setInProgressSkills(inProgress);
    summary.setNotStartedSkills(notStarted);
    summary.setOverallProgressPercentage(overallProgress);
    summary.setLearningPathCompleted(
        totalSkills > 0 && completed == totalSkills
);

    return summary;
}

public LearningPath updateTopicProgress(
        String userId,
        String jobId,
        String skillId,
        String topicName,
        com.skillgap.learning.dto.TopicProgressRequest request) {

    if (request == null || request.getCompleted() == null) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Completion status is required"
        );
    }

    LearningPath path = learningPathRepository
            .findByUserIdAndJobId(userId, jobId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Learning path not found for this job"
            ));

    LearningPathItem item = path.getItems().stream()
            .filter(existing -> skillId.equals(existing.getSkillId()))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Skill not found in this learning path"
            ));

    if (item.getTopics() == null ||
            !item.getTopics().contains(topicName)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Topic not found for this skill"
        );
    }

    if (item.getCompletedTopics() == null) {
        item.setCompletedTopics(new java.util.ArrayList<>());
    }

    if (request.getCompleted()) {
        if (!item.getCompletedTopics().contains(topicName)) {
            item.getCompletedTopics().add(topicName);
        }
    } else {
        item.getCompletedTopics().remove(topicName);
    }

    int totalTopics = item.getTopics().size();
    int completedTopics = item.getCompletedTopics().size();

    int progress = totalTopics == 0
            ? 0
            : (completedTopics * 100) / totalTopics;

    item.setProgressPercentage(progress);

    if (progress == 100) {
        item.setStatus("COMPLETED");
    } else if (progress == 0) {
        item.setStatus("NOT_STARTED");
    } else {
        item.setStatus("IN_PROGRESS");
    }

    path.setUpdatedAt(java.time.Instant.now());

    return learningPathRepository.save(path);
}

}
