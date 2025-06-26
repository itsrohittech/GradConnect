package com.example.gradconnect;

public class Notice {
    private String noticeTitle;
    private String noticeContent;
    private String noticeDate;

    // Constructor
    public Notice(String noticeTitle, String noticeContent, String noticeDate) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeDate = noticeDate;
    }

    // Getters
    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public String getNoticeDate() {
        return noticeDate;
    }
}

