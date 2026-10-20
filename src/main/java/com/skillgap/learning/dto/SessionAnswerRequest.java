
package com.skillgap.learning.dto;

import jakarta.validation.constraints.NotBlank;

public class SessionAnswerRequest {

    @NotBlank
    private String questionId;

    @NotBlank
    private String selectedAnswer;

    public SessionAnswerRequest() {
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}
