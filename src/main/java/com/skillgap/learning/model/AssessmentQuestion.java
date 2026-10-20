package com.skillgap.learning.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "assessmentQuestion")
public class AssessmentQuestion {

    @Id
    private String id;

    private String topicId;

    private String question;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctAnswer;

    private int difficulty;

    public AssessmentQuestion() {
    }

    public AssessmentQuestion(
            String topicId,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer,
            int difficulty
    ) {
        this.topicId = topicId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}