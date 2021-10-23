package com.adam.project.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class QuestionForAnsweringDTO {
    @NotBlank(message = "Вопрос не может быть пустым")
    @Length(max = 2048, message = "Слишком длинный вопрос")
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
