package com.fyp.sarcasmania.app.Classes;

import java.io.Serializable;

public class humorFeedback implements Serializable {

    private int humor;
    private String username;
    private int tweetid;

    public humorFeedback() {
    }

    public humorFeedback(int humor, String username, int tweetid) {
        this.humor = humor;
        this.username = username;
        this.tweetid = tweetid;
    }

    public int getHumor() {
        return humor;
    }

    public void setHumor(int humor) {
        this.humor = humor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTweetid() {
        return tweetid;
    }

    public void setTweetid(int tweetid) {
        this.tweetid = tweetid;
    }
}
