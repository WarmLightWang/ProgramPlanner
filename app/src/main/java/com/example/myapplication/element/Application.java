package com.example.myapplication.element;

public class Application {

    public String appId;
    public String username;
    public String roles;

    public Application(String username, String roles) {
        this.username = username;
        this.roles = roles;
    }

    public Application(String appId, String username, String roles) {
        this.appId = appId;
        this.username = username;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nRole: %s", username, roles);
    }
}
