
package com.skillgap.learning.service;

import com.skillgap.job.model.Job;
import com.skillgap.job.repository.JobRepository;
import com.skillgap.jobRequirement.model.JobRequirement;
import com.skillgap.jobRequirement.repository.JobRequirementRepository;
import com.skillgap.learning.dto.AssessmentSessionResponse;
import com.skillgap.learning.dto.CreateAssessmentSessionRequest;
import com.skillgap.learning.dto.GeneratedQuestion;
import com.skillgap.learning.dto.SessionAnswerRequest;
import com.skillgap.learning.dto.SessionAssessmentResultResponse;
import com.skillgap.learning.dto.SessionQuestionResponse;
import com.skillgap.learning.dto.SubmitSessionAssessmentRequest;
import com.skillgap.learning.model.AssessmentQuestion;
import com.skillgap.learning.model.AssessmentSession;
import com.skillgap.learning.model.SessionQuestion;
import com.skillgap.learning.model.SkillTopic;
import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.AssessmentQuestionRepository;
import com.skillgap.learning.repository.AssessmentSessionRepository;
import com.skillgap.learning.repository.SkillTopicRepository;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import com.skillgap.skill.model.Skill;
import com.skillgap.skill.repository.SkillRepository;
import com.skillgap.learning.dto.AssessmentHistoryResponse;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AssessmentSessionService {

    private final JobRepository jobRepository;
    private final JobRequirementRepository jobRequirementRepository;
    private final SkillRepository skillRepository;
    private final SkillTopicRepository skillTopicRepository;
    private final GroqFallbackAssessmentQuestionService questionService;
    private final AssessmentSessionRepository sessionRepository;
    private final UserTopicKnowledgeRepository userTopicKnowledgeRepository;
    private final AssessmentQuestionRepository assessmentQuestionRepository;

    private static final int MAX_TOPICS_PER_SESSION = 3;
    private static final int QUESTIONS_PER_TOPIC = 2;
    private static final int SUFFICIENT_KNOWLEDGE_LEVEL = 70;
    private static final int SKILL_QUESTIONS_PER_SESSION = 10;

    public AssessmentSessionService(
            JobRepository jobRepository,
            JobRequirementRepository jobRequirementRepository,
            SkillRepository skillRepository,
            SkillTopicRepository skillTopicRepository,
            AssessmentSessionRepository sessionRepository,
            UserTopicKnowledgeRepository userTopicKnowledgeRepository,
            GroqFallbackAssessmentQuestionService questionService,
            AssessmentQuestionRepository assessmentQuestionRepository) {

        this.jobRepository = jobRepository;
        this.jobRequirementRepository = jobRequirementRepository;
        this.skillRepository = skillRepository;
        this.skillTopicRepository = skillTopicRepository;
        this.sessionRepository = sessionRepository;
        this.userTopicKnowledgeRepository = userTopicKnowledgeRepository;
        this.questionService = questionService;
        this.assessmentQuestionRepository = assessmentQuestionRepository;
    }

    // =========================================================
    // CREATE ASSESSMENT
    // No skillId = existing Groq combined assessment.
    // skillId present = individual MongoDB question-bank assessment.
    // =========================================================

    public AssessmentSessionResponse createSession(
            String userId,
            CreateAssessmentSessionRequest request) {

        if (request.getSkillId() != null
                && !request.getSkillId().isBlank()) {

            return createSkillSession(
                    userId,
                    request.getJobId(),
                    request.getSkillId());
        }

        // =====================================================
        // EXISTING COMBINED GROQ ASSESSMENT — PRESERVED
        // =====================================================

        Job job = jobRepository
                .findByIdAndUserId(request.getJobId(), userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Job not found or access denied"));

        List<JobRequirement> requirements =
                jobRequirementRepository.findByJobId(job.getId());

        if (requirements.isEmpty()) {
            throw new IllegalStateException(
                    "This job has no extracted skill requirements");
        }

        Map<String, List<TopicCandidate>> candidatesBySkill =
                new LinkedHashMap<>();

        Map<String, List<TopicCandidate>> allTopicsBySkill =
                new LinkedHashMap<>();

        Set<String> processedSkillIds = new HashSet<>();
        List<String> skillsWithoutTopics = new ArrayList<>();

        for (JobRequirement requirement : requirements) {

            String skillId = requirement.getSkillId();

            if (skillId == null || !processedSkillIds.add(skillId)) {
                continue;
            }

            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Required skill not found: " + skillId));

            List<SkillTopic> topics =
                    skillTopicRepository.findBySkillId(skill.getId());

            if (topics.isEmpty()) {
                skillsWithoutTopics.add(skill.getName());
                continue;
            }

            List<TopicCandidate> allTopics = new ArrayList<>();
            List<TopicCandidate> eligibleTopics = new ArrayList<>();

            for (SkillTopic topic : topics) {

                TopicCandidate candidate =
                        new TopicCandidate(skill, topic);

                allTopics.add(candidate);

                UserTopicKnowledge evidence =
                        userTopicKnowledgeRepository
                                .findByUserIdAndTopicId(
                                        userId,
                                        topic.getId())
                                .orElse(null);

                if (evidence == null
                        || evidence.getKnowledgeLevel()
                                < SUFFICIENT_KNOWLEDGE_LEVEL) {

                    eligibleTopics.add(candidate);
                }
            }

            allTopicsBySkill.put(skill.getId(), allTopics);
            candidatesBySkill.put(skill.getId(), eligibleTopics);
        }

        if (!skillsWithoutTopics.isEmpty()) {
            throw new IllegalStateException(
                    "Assessment cannot be created because these required "
                            + "skills have no topics configured: "
                            + String.join(", ", skillsWithoutTopics));
        }

        List<TopicCandidate> selectedTopics = new ArrayList<>();
        Set<String> selectedTopicIds = new HashSet<>();

        for (Map.Entry<String, List<TopicCandidate>> entry
                : candidatesBySkill.entrySet()) {

            List<TopicCandidate> eligibleTopics = entry.getValue();
            TopicCandidate selectedCandidate;

            if (!eligibleTopics.isEmpty()) {
                selectedCandidate = eligibleTopics.get(0);
            } else {
                List<TopicCandidate> allTopics =
                        allTopicsBySkill.get(entry.getKey());

                if (allTopics == null || allTopics.isEmpty()) {
                    throw new IllegalStateException(
                            "No topics available for skill: "
                                    + entry.getKey());
                }

                selectedCandidate = allTopics.get(0);
            }

            selectedTopics.add(selectedCandidate);
            selectedTopicIds.add(selectedCandidate.topic.getId());
        }

        int round = 1;
        boolean addedInThisRound;

        do {
            addedInThisRound = false;

            for (List<TopicCandidate> skillTopics
                    : candidatesBySkill.values()) {

                if (selectedTopics.size() >= MAX_TOPICS_PER_SESSION) {
                    break;
                }

                if (round < skillTopics.size()) {
                    TopicCandidate candidate = skillTopics.get(round);
                    String topicId = candidate.topic.getId();

                    if (selectedTopicIds.add(topicId)) {
                        selectedTopics.add(candidate);
                        addedInThisRound = true;
                    }
                }
            }

            round++;

        } while (addedInThisRound
                && selectedTopics.size() < MAX_TOPICS_PER_SESSION);

        if (selectedTopics.size() > MAX_TOPICS_PER_SESSION) {
            selectedTopics = new ArrayList<>(
                    selectedTopics.subList(0, MAX_TOPICS_PER_SESSION));
        }

        if (selectedTopics.isEmpty()) {
            throw new IllegalStateException(
                    "No topics are available for this job assessment");
        }

        List<CompletableFuture<List<GeneratedQuestion>>> futures =
                selectedTopics.stream()
                        .map(candidate -> CompletableFuture.supplyAsync(
                                () -> questionService.generateQuestions(
                                        candidate.skill.getName(),
                                        candidate.topic.getName(),
                                        QUESTIONS_PER_TOPIC)))
                        .toList();

        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture<?>[0])).join();

        List<SessionQuestion> savedQuestions = new ArrayList<>();

        for (int i = 0; i < selectedTopics.size(); i++) {

            TopicCandidate candidate = selectedTopics.get(i);
            Skill skill = candidate.skill;
            SkillTopic topic = candidate.topic;

            List<GeneratedQuestion> generatedQuestions =
                    futures.get(i).join();

            if (generatedQuestions == null
                    || generatedQuestions.size() != QUESTIONS_PER_TOPIC) {

                throw new IllegalStateException(
                        "Could not generate the required number of questions "
                                + "for skill " + skill.getName()
                                + ", topic " + topic.getName());
            }

            for (GeneratedQuestion generatedQuestion : generatedQuestions) {

                SessionQuestion question = new SessionQuestion();

                // Groq questions use generated IDs.
                question.setQuestionId(UUID.randomUUID().toString());
                question.setSkillId(skill.getId());
                question.setTopicId(topic.getId());
                question.setQuestion(generatedQuestion.getQuestion());
                question.setOptionA(generatedQuestion.getOptionA());
                question.setOptionB(generatedQuestion.getOptionB());
                question.setOptionC(generatedQuestion.getOptionC());
                question.setOptionD(generatedQuestion.getOptionD());

                // Correct answer stays on the server.
                question.setCorrectAnswer(
                        generatedQuestion.getCorrectAnswer());
                question.setDifficulty(
                        generatedQuestion.getDifficulty());

                savedQuestions.add(question);
            }
        }

        if (savedQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No assessment questions could be generated");
        }

        AssessmentSession session = new AssessmentSession();
        session.setUserId(userId);
        session.setJobId(job.getId());
        session.setStatus("IN_PROGRESS");
        session.setCreatedAt(Instant.now());
        session.setQuestions(savedQuestions);

        AssessmentSession savedSession =
                sessionRepository.save(session);

        return toResponse(savedSession);
    }

    // =========================================================
    // INDIVIDUAL SKILL ASSESSMENT FROM MONGODB
    // =========================================================

    private AssessmentSessionResponse createSkillSession(
            String userId,
            String jobId,
            String skillId) {

        // 1. Verify job ownership.
        Job job = jobRepository
                .findByIdAndUserId(jobId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Job not found or access denied"));

        // 2. Confirm that this skill belongs to the job.
        List<JobRequirement> requirements =
                jobRequirementRepository.findByJobId(job.getId());

        boolean skillRequiredByJob = requirements.stream()
                .anyMatch(requirement ->
                        skillId.equals(requirement.getSkillId()));

        if (!skillRequiredByJob) {
            throw new IllegalArgumentException(
                    "This skill is not associated with the selected job");
        }

        // 3. Load the selected skill.
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Skill not found: " + skillId));

        // 4. Load only this skill's topics.
        List<SkillTopic> topics =
                skillTopicRepository.findBySkillId(skillId);

        if (topics.isEmpty()) {
            throw new IllegalStateException(
                    "No topics are configured for skill: "
                            + skill.getName());
        }

        // 5. Gather questions from MongoDB for those topics.
        List<AssessmentQuestion> availableQuestions =
                new ArrayList<>();

        Map<String, String> topicIdByQuestionId =
                new HashMap<>();

        for (SkillTopic topic : topics) {

            List<AssessmentQuestion> topicQuestions =
                    assessmentQuestionRepository.findByTopicId(
                            topic.getId());

            for (AssessmentQuestion question : topicQuestions) {

                if (question.getId() == null
                        || question.getQuestion() == null
                        || question.getCorrectAnswer() == null) {
                    continue;
                }

                availableQuestions.add(question);
                topicIdByQuestionId.put(
                        question.getId(),
                        topic.getId());
            }
        }

        if (availableQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No question-bank questions are available for skill: "
                            + skill.getName());
        }

        // 6. Collect MongoDB question IDs used in previous sessions
        // for this same skill.
        Set<String> previouslyAttemptedQuestionIds = new HashSet<>();

        List<AssessmentSession> previousSessions =
                sessionRepository.findByUserId(userId);

        if (previousSessions != null) {
            for (AssessmentSession previousSession : previousSessions) {

                if (previousSession.getQuestions() == null) {
                    continue;
                }

                for (SessionQuestion previousQuestion
                        : previousSession.getQuestions()) {

                    if (skillId.equals(previousQuestion.getSkillId())
                            && previousQuestion.getQuestionId() != null) {

                        previouslyAttemptedQuestionIds.add(
                                previousQuestion.getQuestionId());
                    }
                }
            }
        }

        // 7. Prefer questions the user has not attempted.
        List<AssessmentQuestion> unseenQuestions = new ArrayList<>();
        List<AssessmentQuestion> seenQuestions = new ArrayList<>();

        for (AssessmentQuestion question : availableQuestions) {

            if (previouslyAttemptedQuestionIds.contains(
                    question.getId())) {

                seenQuestions.add(question);
            } else {
                unseenQuestions.add(question);
            }
        }

        Collections.shuffle(unseenQuestions);
        Collections.shuffle(seenQuestions);

        List<AssessmentQuestion> selectedQuestions =
                new ArrayList<>();

        // First choose unseen questions.
        int unseenCount = Math.min(
                SKILL_QUESTIONS_PER_SESSION,
                unseenQuestions.size());

        selectedQuestions.addAll(
                unseenQuestions.subList(0, unseenCount));

        // If fewer than 10 unseen questions exist, fill the remainder
        // with previously attempted questions.
        int remaining = SKILL_QUESTIONS_PER_SESSION
                - selectedQuestions.size();

        if (remaining > 0) {
            int seenCount = Math.min(remaining, seenQuestions.size());

            selectedQuestions.addAll(
                    seenQuestions.subList(0, seenCount));
        }

        if (selectedQuestions.isEmpty()) {
            throw new IllegalStateException(
                    "No questions could be selected for this skill");
        }

        // 8. Save the selected MongoDB questions in the session.
        // Keep their original database IDs to track repeated attempts.
        List<SessionQuestion> sessionQuestions = new ArrayList<>();

        for (AssessmentQuestion bankQuestion : selectedQuestions) {

            SessionQuestion question = new SessionQuestion();

            question.setQuestionId(bankQuestion.getId());
            question.setSkillId(skill.getId());
            question.setTopicId(
                    topicIdByQuestionId.get(bankQuestion.getId()));

            question.setQuestion(bankQuestion.getQuestion());
            question.setOptionA(bankQuestion.getOptionA());
            question.setOptionB(bankQuestion.getOptionB());
            question.setOptionC(bankQuestion.getOptionC());
            question.setOptionD(bankQuestion.getOptionD());

            // Saved in the session, never exposed by toResponse().
            question.setCorrectAnswer(
                    bankQuestion.getCorrectAnswer());
            question.setDifficulty(bankQuestion.getDifficulty());

            sessionQuestions.add(question);
        }

        AssessmentSession session = new AssessmentSession();
        session.setUserId(userId);
        session.setJobId(job.getId());
        session.setStatus("IN_PROGRESS");
        session.setCreatedAt(Instant.now());
        session.setQuestions(sessionQuestions);

        AssessmentSession savedSession =
                sessionRepository.save(session);

        return toResponse(savedSession);
    }

    // =========================================================
    // CONVERT SESSION TO A SAFE RESPONSE
    // =========================================================

    private AssessmentSessionResponse toResponse(
            AssessmentSession session) {

        AssessmentSessionResponse response =
                new AssessmentSessionResponse();

        response.setSessionId(session.getId());
        response.setJobId(session.getJobId());
        response.setStatus(session.getStatus());
        response.setCreatedAt(session.getCreatedAt());

        List<SessionQuestionResponse> responses = new ArrayList<>();

        for (SessionQuestion question : session.getQuestions()) {

            SessionQuestionResponse item =
                    new SessionQuestionResponse();

            item.setQuestionId(question.getQuestionId());
            item.setSkillId(question.getSkillId());
            item.setTopicId(question.getTopicId());
            item.setQuestion(question.getQuestion());
            item.setOptionA(question.getOptionA());
            item.setOptionB(question.getOptionB());
            item.setOptionC(question.getOptionC());
            item.setOptionD(question.getOptionD());
            item.setDifficulty(question.getDifficulty());

            // Never send correct answers to the frontend.
            responses.add(item);
        }

        response.setQuestions(responses);
        return response;
    }

    // =========================================================
    // SUBMIT AND SCORE ASSESSMENT
    // Shared by Groq and MongoDB sessions.
    // =========================================================

    public SessionAssessmentResultResponse submitSession(
            String userId,
            SubmitSessionAssessmentRequest request) {

        AssessmentSession session = sessionRepository
                .findByIdAndUserId(request.getSessionId(), userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Assessment session not found or access denied"));

        if (!"IN_PROGRESS".equals(session.getStatus())) {
            throw new IllegalStateException(
                    "This assessment session has already been submitted");
        }

        List<SessionQuestion> questions = session.getQuestions();

        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException(
                    "Assessment session contains no questions");
        }

        Map<String, SessionQuestion> questionMap = new HashMap<>();

        for (SessionQuestion question : questions) {

            if (questionMap.put(
                    question.getQuestionId(), question) != null) {

                throw new IllegalStateException(
                        "Assessment session contains duplicate question IDs");
            }
        }

        if (request.getAnswers() == null
                || request.getAnswers().isEmpty()) {

            throw new IllegalArgumentException(
                    "Answers are required to submit the assessment");
        }

        Set<String> submittedQuestionIds = new HashSet<>();

        for (SessionAnswerRequest answer : request.getAnswers()) {

            if (answer == null
                    || answer.getQuestionId() == null
                    || answer.getSelectedAnswer() == null) {

                throw new IllegalArgumentException(
                        "Every answer must include a question ID and selection");
            }

            String questionId = answer.getQuestionId();

            String selected = answer.getSelectedAnswer()
                    .trim()
                    .toUpperCase();

            if (!submittedQuestionIds.add(questionId)) {
                throw new IllegalArgumentException(
                        "Duplicate answer for question: " + questionId);
            }

            if (!questionMap.containsKey(questionId)) {
                throw new IllegalArgumentException(
                        "Question does not belong to this assessment session");
            }

            if (!Set.of("A", "B", "C", "D").contains(selected)) {
                throw new IllegalArgumentException(
                        "Selected answer must be A, B, C, or D");
            }
        }

        if (submittedQuestionIds.size() != questionMap.size()) {
            throw new IllegalArgumentException(
                    "Submit exactly one answer for every assessment question");
        }

        class TopicScore {
            String skillId;
            String topicId;
            int total;
            int correct;
        }

        Map<String, TopicScore> topicScores = new LinkedHashMap<>();
        int correctAnswers = 0;

        for (SessionAnswerRequest answer : request.getAnswers()) {

            SessionQuestion question =
                    questionMap.get(answer.getQuestionId());

            String selected = answer.getSelectedAnswer()
                    .trim()
                    .toUpperCase();

            boolean correct = selected.equalsIgnoreCase(
                    question.getCorrectAnswer());

            if (correct) {
                correctAnswers++;
            }

            String topicId = question.getTopicId();

            if (topicId == null || topicId.isBlank()) {
                throw new IllegalStateException(
                        "Assessment question has no topic ID");
            }

            TopicScore topicScore = topicScores.computeIfAbsent(
                    topicId,
                    ignored -> {
                        TopicScore score = new TopicScore();
                        score.skillId = question.getSkillId();
                        score.topicId = topicId;
                        return score;
                    });

            if (!topicScore.skillId.equals(question.getSkillId())) {
                throw new IllegalStateException(
                        "Inconsistent skill IDs for assessment topic");
            }

            topicScore.total++;

            if (correct) {
                topicScore.correct++;
            }
        }

        int totalQuestions = questions.size();

        int scorePercentage = (int) Math.round(
                correctAnswers * 100.0 / totalQuestions);

        List<SessionAssessmentResultResponse.TopicAssessmentResult>
                topicResults = new ArrayList<>();

        for (TopicScore topicScore : topicScores.values()) {

            int knowledgeLevel = (int) Math.round(
                    topicScore.correct * 100.0 / topicScore.total);

            UserTopicKnowledge knowledge =
                    userTopicKnowledgeRepository
                            .findByUserIdAndTopicId(
                                    userId,
                                    topicScore.topicId)
                            .orElseGet(() ->
                                    new UserTopicKnowledge(
                                            userId,
                                            topicScore.topicId,
                                            knowledgeLevel));

            // Preserve the existing strongest-observed-knowledge behavior.
            knowledge.setKnowledgeLevel(
                    Math.max(
                            knowledge.getKnowledgeLevel(),
                            knowledgeLevel));

            userTopicKnowledgeRepository.save(knowledge);

            SessionAssessmentResultResponse.TopicAssessmentResult result =
                    new SessionAssessmentResultResponse
                            .TopicAssessmentResult();

            result.setSkillId(topicScore.skillId);
            result.setTopicId(topicScore.topicId);
            result.setTotalQuestions(topicScore.total);
            result.setCorrectAnswers(topicScore.correct);
            result.setKnowledgeLevel(knowledgeLevel);

            topicResults.add(result);
        }

        session.setScorePercentage(scorePercentage);
        session.setSubmittedAt(Instant.now());
        session.setStatus("SUBMITTED");

        AssessmentSession savedSession =
                sessionRepository.save(session);

        SessionAssessmentResultResponse response =
                new SessionAssessmentResultResponse();

        response.setSessionId(savedSession.getId());
        response.setJobId(savedSession.getJobId());
        response.setStatus(savedSession.getStatus());
        response.setTotalQuestions(totalQuestions);
        response.setCorrectAnswers(correctAnswers);
        response.setScorePercentage(scorePercentage);
        response.setSubmittedAt(savedSession.getSubmittedAt());
        response.setTopicResults(topicResults);

        return response;
    }

    // =========================================================
    // INTERNAL TOPIC CANDIDATE
    // =========================================================

    private static class TopicCandidate {

        private final Skill skill;
        private final SkillTopic topic;

        private TopicCandidate(Skill skill, SkillTopic topic) {
            this.skill = skill;
            this.topic = topic;
        }
    }
    
public List<AssessmentHistoryResponse> getAssessmentHistory(
        String userId,
        String jobId
) {
    return sessionRepository
            .findByUserIdAndJobId(userId, jobId)
            .stream()
            .sorted(
                    Comparator.comparing(
                            AssessmentSession::getCreatedAt,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    )
            )
            .map(session -> {
                AssessmentHistoryResponse response =
                        new AssessmentHistoryResponse();

                response.setSessionId(session.getId());
                response.setJobId(session.getJobId());
                response.setStatus(session.getStatus());
                response.setCreatedAt(session.getCreatedAt());
                response.setSubmittedAt(session.getSubmittedAt());
                response.setScorePercentage(session.getScorePercentage());
                response.setTotalQuestions(
                        session.getQuestions() == null
                                ? 0
                                : session.getQuestions().size()
                );

                return response;
            })
            .toList();
}

}
