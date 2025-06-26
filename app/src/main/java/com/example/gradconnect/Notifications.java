package com.example.gradconnect;

public class Notifications {
    private String title;
    private String content;
    private String date;

    // Constructor
    public Notifications(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
