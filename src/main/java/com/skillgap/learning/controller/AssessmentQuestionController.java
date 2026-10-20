package com.skillgap.learning.controller;

import com.skillgap.learning.dto.AssessmentQuestionResponse;
import com.skillgap.learning.model.AssessmentQuestion;
import com.skillgap.learning.service.AssessmentQuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning/assessment")
public class AssessmentQuestionController {

    private final AssessmentQuestionService assessmentQuestionService;

    public AssessmentQuestionController(
            AssessmentQuestionService assessmentQuestionService
    ) {
        this.assessmentQuestionService = assessmentQuestionService;
    }

    @GetMapping("/topics/{topicId}/questions")
    public ResponseEntity<List<AssessmentQuestionResponse>> getQuestionsByTopic(
            @PathVariable String topicId
    ) {
        List<AssessmentQuestion> questions =
                assessmentQuestionService.getQuestionsByTopicId(topicId);

        List<AssessmentQuestionResponse> responses =
                questions.stream()
                        .map(question -> new AssessmentQuestionResponse(
                                question.getId(),
                                question.getTopicId(),
                                question.getQuestion(),
                                question.getOptionA(),
                                question.getOptionB(),
                                question.getOptionC(),
                                question.getOptionD(),
                                question.getDifficulty()
                        ))
                        .toList();

        return ResponseEntity.ok(responses);
    }
    
}   