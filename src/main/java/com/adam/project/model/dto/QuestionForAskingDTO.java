package com.adam.project.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class QuestionForAskingDTO {

    @NotBlank(message = "Вопрос не может быть пустым")
    @Length(max = 2048, message = "Слишком длинный вопрос")
    private String question;

    @NotBlank(message = "Тема не может быть пустой")
    @Length(max = 50, message = "Слишком длинная тема")
    private String topic;

    private boolean isAnonymous;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
