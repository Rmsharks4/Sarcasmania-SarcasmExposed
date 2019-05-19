package com.fyp.sarcasmania.app.Classes;

import java.io.Serializable;

public class insultFeedback implements Serializable {

    private int insult;
    private String username;
    private int tweetid;

    public insultFeedback() {
    }

    public insultFeedback(int insult, String username, int tweetid) {
        this.insult = insult;
        this.username = username;
        this.tweetid = tweetid;
    }

    public int getInsult() {
        return insult;
    }

    public void setInsult(int insult) {
        this.insult = insult;
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
