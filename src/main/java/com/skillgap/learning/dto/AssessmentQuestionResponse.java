package com.skillgap.learning.dto;

public class AssessmentQuestionResponse {

    private String id;

    private String topicId;

    private String question;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private int difficulty;

    public AssessmentQuestionResponse(
            String id,
            String topicId,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            int difficulty
    ) {
        this.id = id;
        this.topicId = topicId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
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

    public int getDifficulty() {
        return difficulty;
    }
}   