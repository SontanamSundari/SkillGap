package com.skillgap.learning.service;

import com.skillgap.learning.dto.AssessmentAnswer;
import com.skillgap.learning.dto.AssessmentResultResponse;
import com.skillgap.learning.dto.SubmitAssessmentRequest;
import com.skillgap.learning.model.AssessmentQuestion;
import com.skillgap.learning.model.UserTopicKnowledge;
import com.skillgap.learning.repository.AssessmentQuestionRepository;
import com.skillgap.learning.repository.UserTopicKnowledgeRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

import java.util.List;

@Service
public class AssessmentQuestionService {

        private final AssessmentQuestionRepository questionRepository;
        private final UserTopicKnowledgeRepository knowledgeRepository;

        public AssessmentQuestionService(
                        AssessmentQuestionRepository questionRepository,
                        UserTopicKnowledgeRepository knowledgeRepository) {
                this.questionRepository = questionRepository;
                this.knowledgeRepository = knowledgeRepository;
        }

        public List<AssessmentQuestion> getQuestionsByTopicId(
                        String topicId) {
                return questionRepository.findByTopicId(topicId);
        }

        public AssessmentQuestion getQuestionById(String questionId) {
                return questionRepository.findById(questionId)
                                .orElseThrow(() -> new RuntimeException("Assessment question not found"));
        }

        public boolean isCorrectAnswer(
                        String questionId,
                        String selectedAnswer) {
                AssessmentQuestion question = getQuestionById(questionId);

                return question.getCorrectAnswer()
                                .equalsIgnoreCase(selectedAnswer.trim());
        }

        public AssessmentResultResponse submitAssessment(
                        String userId,
                        SubmitAssessmentRequest request) {
                List<AssessmentQuestion> questions = questionRepository.findByTopicId(request.getTopicId());

                if (questions.isEmpty()) {
                        throw new RuntimeException(
                                        "No assessment questions found for this topic");
                }

                int correctAnswers = 0;
                if (request.getAnswers().size() != questions.size()) {
                        throw new RuntimeException(
                                        "Please answer all assessment questions");
                }

Set<String> expectedQuestionIds = new HashSet<>();

for (AssessmentQuestion question : questions) {
    expectedQuestionIds.add(question.getId());
}

Set<String> submittedQuestionIds = new HashSet<>();

for (AssessmentAnswer answer : request.getAnswers()) {
    if (!submittedQuestionIds.add(answer.getQuestionId())) {
        throw new RuntimeException(
                "Duplicate question answer submitted: "
                        + answer.getQuestionId()
        );
    }
}

if (!expectedQuestionIds.equals(submittedQuestionIds)) {
    throw new RuntimeException(
            "Submitted answers do not match the questions for this topic"
    );
}


                for (AssessmentAnswer answer : request.getAnswers()) {

                        AssessmentQuestion question = questionRepository.findById(answer.getQuestionId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Assessment question not found: "
                                                                        + answer.getQuestionId()));

                        if (!question.getTopicId().equals(request.getTopicId())) {
                                throw new RuntimeException(
                                                "Question does not belong to the selected topic");
                        }

                        if (question.getCorrectAnswer()
                                        .equalsIgnoreCase(answer.getSelectedAnswer().trim())) {

                                correctAnswers++;
                        }
                }

                int totalQuestions = questions.size();

                int scorePercentage = (int) Math.round(
                                ((double) correctAnswers / totalQuestions) * 100);

                int knowledgeLevel = calculateKnowledgeLevel(
                                correctAnswers,
                                totalQuestions);

                UserTopicKnowledge knowledge = knowledgeRepository
                                .findByUserIdAndTopicId(
                                                userId,
                                                request.getTopicId())
                                .orElse(
                                                new UserTopicKnowledge(
                                                                userId,
                                                                request.getTopicId(),
                                                                knowledgeLevel));

                knowledge.setKnowledgeLevel(knowledgeLevel);

                knowledgeRepository.save(knowledge);

                return new AssessmentResultResponse(
                                request.getTopicId(),
                                correctAnswers,
                                totalQuestions,
                                scorePercentage,
                                knowledgeLevel);
        }

        private int calculateKnowledgeLevel(
                        int correctAnswers,
                        int totalQuestions) {
                double percentage = ((double) correctAnswers / totalQuestions) * 100;

                if (percentage >= 80) {
                        return 5;
                }

                if (percentage >= 60) {
                        return 4;
                }

                if (percentage >= 40) {
                        return 3;
                }

                if (percentage >= 20) {
                        return 2;
                }

                return 1;
        }
}