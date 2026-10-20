
package com.skillgap.learning.dto;

public class SessionQuestionResponse {

    private String questionId;
    private String skillId;
    private String topicId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int difficulty;

    public SessionQuestionResponse() {
    }

    public SessionQuestionResponse(
            String questionId,
            String skillId,
            String topicId,
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            int difficulty
    ) {
        this.questionId = questionId;
        this.skillId = skillId;
        this.topicId = topicId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.difficulty = difficulty;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getSkillId() {
        return skillId;
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

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
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

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
