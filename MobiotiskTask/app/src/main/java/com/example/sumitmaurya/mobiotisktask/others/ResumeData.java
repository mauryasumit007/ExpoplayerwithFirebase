package com.example.sumitmaurya.mobiotisktask.others;

public class ResumeData {

    public ResumeData(String videoUrl, String duration) {
        this.videoUrl = videoUrl;
        this.duration = duration;
    }

    String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    String duration;

}
