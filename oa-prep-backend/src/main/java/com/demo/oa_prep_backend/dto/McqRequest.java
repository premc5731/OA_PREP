package com.demo.oa_prep_backend.dto;

public class McqRequest {
    private String topic;
    private int count;

    // Getters and Setters are required for Jackson to deserialize the JSON.
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
