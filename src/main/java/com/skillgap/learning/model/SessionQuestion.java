
package com.skillgap.learning.model;

public class SessionQuestion {

    private String questionId;

    private String skillId;

    private String topicId;

    private String question;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String correctAnswer;

    private int difficulty;

    public SessionQuestion() {
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

    public String getCorrectAnswer() {
        return correctAnswer;
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

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
