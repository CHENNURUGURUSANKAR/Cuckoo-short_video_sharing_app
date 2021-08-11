package com.gurusankar149.cuckoo;

public class VideoModel {
    String VideoTitle,VideoUri,VideoDescription;

    public VideoModel(String videoTitle, String videoUri, String videoDescription) {
        VideoTitle = videoTitle;
        VideoUri = videoUri;
        VideoDescription = videoDescription;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public String getVideoUri() {
        return VideoUri;
    }

    public void setVideoUri(String videoUri) {
        VideoUri = videoUri;
    }

    public String getVideoDescription() {
        return VideoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        VideoDescription = videoDescription;
    }
}
