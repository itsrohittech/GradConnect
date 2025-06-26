package com.example.gradconnect;

public class Event {
    private String eventTitle;
    private String date;
    private String eventContent;

    public Event(String eventTitle, String date, String eventContent) {
        this.eventTitle = eventTitle;
        this.date = date;
        this.eventContent = eventContent;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getDate() {
        return date;
    }

    public String getEventContent() {
        return eventContent;
    }
}

