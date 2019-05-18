package com.fyp.sarcasmania.app.Classes;

import java.io.Serializable;

public class report implements Serializable {
    private int tweetid;
    private String tweet;
    private String username;

    public report() {
    }

    public report(int tweetid, String tweet, String username) {
        this.tweetid = tweetid;
        this.tweet = tweet;
        this.username = username;
    }

    public int getTweetid() {
        return tweetid;
    }

    public void setTweetid(int tweetid) {
        this.tweetid = tweetid;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
