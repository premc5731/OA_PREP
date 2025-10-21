package com.demo.oa_prep_backend.dto;

import java.util.List;

// This class represents the structure of a single MCQ.
// It matches the format expected by the React frontend.
public class Question {
    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
