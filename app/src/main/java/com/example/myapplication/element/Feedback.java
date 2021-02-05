package com.example.myapplication.element;

public class Feedback {

    public String username;
    public String comment;

    public Feedback() {
        username = "";
        comment = "";
    }

    public Feedback(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return this.comment;
    }
}
