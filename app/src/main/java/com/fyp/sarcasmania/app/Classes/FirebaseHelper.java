package com.fyp.sarcasmania.app.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static DatabaseReference databaseReference;

    public FirebaseHelper() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void newUser(String username, String fullname, String email, String password, String picture, String type) {
        User user = new User(username, fullname, email, password, picture, type);
        databaseReference.child("Users").child(user.getUsername()).setValue(user);
    }

    public void newPost(int tweetID, String tweet, String username, float sarcasm, int humor, int insult, String time) {
        Post post = new Post(tweetID, tweet, username, sarcasm, humor, insult, time);
        databaseReference.child("Posts").child(Integer.toString(post.getTweetID())).setValue(post);
    }

    public void humorFeed(int humor, String username, int tweetID) {
        humorFeedback humorFeedback = new humorFeedback(humor, username, tweetID);
        databaseReference.child("HumorFeedback").child(tweetID + "-" + username).setValue(humorFeedback);
    }

    public void insultFeed(int insult, String username, int tweetID) {
        insultFeedback insultFeedback = new insultFeedback(insult, username, tweetID);
        databaseReference.child("InsultFeedback").child(tweetID + "-" + username).setValue(insultFeedback);
    }

    public void newReport(int tweetid, String tweet, String username) {
        report report = new report(tweetid, tweet, username);
        databaseReference.child("ReportedTweets").child(Integer.toString(tweetid)).setValue(report);
    }
}
