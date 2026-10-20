package com.skillgap.gap.service;

import com.skillgap.gap.dto.SkillGapResult;
import com.skillgap.jobRequirement.model.JobRequirement;
import com.skillgap.jobRequirement.repository.JobRequirementRepository;
import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;
import com.skillgap.userSkill.model.UserSkill;
import com.skillgap.userSkill.repository.UserSkillRepository;
import org.springframework.stereotype.Service;
import com.skillgap.common.exception.JobNotFoundException;
import com.skillgap.job.repository.JobRepository;
import com.skillgap.gap.dto.SkillGapSummary;
import com.skillgap.gap.dto.SkillRecommendation;
import java.util.Comparator;
import com.skillgap.jobRequirement.model.RequirementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.SkillTopicRepository;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import java.util.HashSet;
import java.util.Set;

@Service
public class SkillGapService {

private final JobRequirementRepository jobRequirementRepository;
private final UserSkillRepository userSkillRepository;
private final SkillRepository skillRepository;
private final JobRepository jobRepository;
private final SkillTopicRepository skillTopicRepository;
private final UserTopicKnowledgeRepository userTopicKnowledgeRepository;

public SkillGapService(
        JobRequirementRepository jobRequirementRepository,
        UserSkillRepository userSkillRepository,
        SkillRepository skillRepository,
        JobRepository jobRepository,
        SkillTopicRepository skillTopicRepository,
        UserTopicKnowledgeRepository userTopicKnowledgeRepository
) {
    this.jobRequirementRepository = jobRequirementRepository;
    this.userSkillRepository = userSkillRepository;
    this.skillRepository = skillRepository;
    this.jobRepository = jobRepository;
    this.skillTopicRepository = skillTopicRepository;
    this.userTopicKnowledgeRepository = userTopicKnowledgeRepository;
}
    
    public List<SkillGapResult> analyzeGap(
            String userId,
            String jobId
    ) {
jobRepository.findByIdAndUserId(jobId, userId)
    .orElseThrow(() ->
        new JobNotFoundException(
            "Job not found with id: " + jobId
        )
    );

        List<JobRequirement> requirements =
                jobRequirementRepository.findByJobId(jobId);
    
        List<UserSkill> userSkills =
                userSkillRepository.findByUserId(userId);

        Map<String, UserSkill> userSkillMap = new HashMap<>();

        for (UserSkill userSkill : userSkills) {
            userSkillMap.put(
                    userSkill.getSkillId(),
                    userSkill
            );
        }

        List<SkillGapResult> results = new ArrayList<>();

        for (JobRequirement requirement : requirements) {

            Skill skill = skillRepository
                    .findById(requirement.getSkillId())
                    .orElse(null);

            if (skill == null) {
                continue;
            }

            UserSkill userSkill =
        userSkillMap.get(requirement.getSkillId());

Integer selfReportedProficiency =
        userSkill != null
                ? userSkill.getProficiency()
                : null;

Integer assessmentProficiency =
        getAssessmentProficiency(
                userId,
                requirement.getSkillId()
        );

// Prefer assessment evidence when available.
// Otherwise, fall back to self-reported proficiency.
Integer userProficiency =
        assessmentProficiency != null
                ? assessmentProficiency
                : selfReportedProficiency;

            String status;

            if (userProficiency == null) {

                status = "MISSING";

            } else if (userProficiency >=
                    requirement.getRequiredProficiency()) {

                status = "MATCHED";

            } else {

                status = "WEAK";
            }

            results.add(
                    new SkillGapResult(
                            skill.getId(),
                            skill.getName(),
                            userProficiency,
                            requirement.getRequiredProficiency(),
                            status,
                            requirement.getRequirementType()
                    )
            );
        }

        return results;
    }
public SkillGapSummary analyzeGapSummary(
        String userId,
        String jobId
) {

    List<SkillGapResult> results =
            analyzeGap(userId, jobId);

    int matchedSkills = 0;
    int weakSkills = 0;
    int missingSkills = 0;
    int criticalMissingSkills = 0;
    int criticalWeakSkills = 0;
    double totalWeight = 0;
    double earnedWeight = 0;

    for (SkillGapResult result : results) {

        int weight;

switch (result.getRequirementType()) {

    case PREFERRED:
        weight = 1;
        break;

    case MUST_HAVE:
        weight = 2;
        break;

    default:
        throw new IllegalStateException(
                "Unexpected requirement type: "
                        + result.getRequirementType()
        );
}

        totalWeight += weight;

        switch (result.getStatus()) {

            case "MATCHED":
                matchedSkills++;
                earnedWeight += weight;
                break;

            case "WEAK":
                weakSkills++;

                if (result.getRequirementType() == RequirementType.MUST_HAVE) {
                    criticalWeakSkills++;
                }

                if (result.getUserProficiency() != null
                        && result.getRequiredProficiency() > 0) {

                    double proficiencyRatio =
                            (double) result.getUserProficiency()
                                    / result.getRequiredProficiency();

                    earnedWeight +=
                            weight * Math.min(proficiencyRatio, 1.0);
                }

    break;

        case "MISSING":
            missingSkills++;

            if (result.getRequirementType() == RequirementType.MUST_HAVE) {
                criticalMissingSkills++;
            }

            break;
        }
    }

    int gapScore = 0;

    if (totalWeight > 0) {

        gapScore = (int) Math.round(
                (earnedWeight / totalWeight) * 100
        );
    }

    int totalRequiredSkills = results.size();

    int matchPercentage = 0;

    if (totalRequiredSkills > 0) {

        matchPercentage =
                (matchedSkills * 100) / totalRequiredSkills;
    }
    String readinessLevel;

if (gapScore >= 80) {
    readinessLevel = "READY";
} else if (gapScore >= 60) {
    readinessLevel = "NEARLY_READY";
} else if (gapScore >= 40) {
    readinessLevel = "NEEDS_IMPROVEMENT";
} else {
    readinessLevel = "NOT_READY";
}
return new SkillGapSummary(
        jobId,
        matchPercentage,
        gapScore,
        readinessLevel,
        matchedSkills,
        weakSkills,
        missingSkills,
        criticalMissingSkills,
        criticalWeakSkills,
        totalRequiredSkills,
        results
);
}

public List<SkillRecommendation> getRecommendations(
        String userId,
        String jobId
) {

    List<SkillGapResult> results =
            analyzeGap(userId, jobId);

    List<SkillRecommendation> recommendations =
            new ArrayList<>();

    for (SkillGapResult result : results) {

        if ("MATCHED".equals(result.getStatus())) {
            continue;
        }

        boolean mustHave =
                !RequirementType.PREFERRED.equals(result.getRequirementType());

        int priority;

        if (mustHave && "MISSING".equals(result.getStatus())) {
            priority = 1;
        } else if (mustHave && "WEAK".equals(result.getStatus())) {
            priority = 2;
        } else if (!mustHave && "MISSING".equals(result.getStatus())) {
            priority = 3;
        } else {
            priority = 4;
        }

int proficiencyGap;

if (result.getUserProficiency() == null) {
    proficiencyGap = result.getRequiredProficiency();
} else {
    proficiencyGap =
            result.getRequiredProficiency()
                    - result.getUserProficiency();
}
String recommendationReason;

if ("MISSING".equals(result.getStatus())) {

    recommendationReason =
            "Learn " + result.getSkillName()
                    + " to reach the required proficiency level of "
                    + result.getRequiredProficiency() + ".";

} else {

    recommendationReason =
            "Improve " + result.getSkillName()
                    + " by " + proficiencyGap
                    + " proficiency level"
                    + (proficiencyGap == 1 ? "" : "s")
                    + " to meet the job requirement.";
}
String impact;

if (result.getRequirementType() == RequirementType.MUST_HAVE) {
    impact = "CRITICAL";
} else {
    impact = "IMPORTANT";
}

recommendations.add(
        new SkillRecommendation(
                result.getSkillId(),
                result.getSkillName(),
                result.getStatus(),
                result.getRequirementType(),
                result.getUserProficiency(),
                result.getRequiredProficiency(),
                proficiencyGap,
                recommendationReason,
                impact,
                priority
        )
);
    }

    recommendations.sort(
            Comparator.comparingInt(
                    SkillRecommendation::getPriority
            )
    );

    return recommendations;
}

private Integer getAssessmentProficiency(
        String userId,
        String skillId
) {
    List<SkillTopic> topics =
            skillTopicRepository.findBySkillId(skillId);

    int totalKnowledge = 0;
    int assessedTopics = 0;

    Set<String> processedTopicIds = new HashSet<>();

    for (SkillTopic topic : topics) {

        if (!processedTopicIds.add(topic.getId())) {
            continue;
        }

        UserTopicKnowledge evidence =
                userTopicKnowledgeRepository
                        .findByUserIdAndTopicId(
                                userId,
                                topic.getId()
                        )
                        .orElse(null);

        // Unassessed topics do not count as zero.
        if (evidence == null) {
            continue;
        }

        totalKnowledge += evidence.getKnowledgeLevel();
        assessedTopics++;
    }

    // null means no assessment evidence exists.
    if (assessedTopics == 0) {
        return null;
    }

   double averageScore =
        (double) totalKnowledge / assessedTopics;

// Convert assessment percentage (0–100)
// to proficiency level (1–5).
int proficiencyLevel =
        (int) Math.ceil(averageScore / 20.0);

return Math.max(1, Math.min(5, proficiencyLevel));
}

}