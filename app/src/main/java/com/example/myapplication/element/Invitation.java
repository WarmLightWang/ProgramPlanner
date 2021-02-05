package com.example.myapplication.element;

public class Invitation {

    public String inviteId;
    public String projectId;
    public String projectName;
    public String projectRole;

    public Invitation(String inviteId, String projectId, String projectName, String projectRole) {
        this.inviteId = inviteId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectRole = projectRole;
    }

    public Invitation(String projectId, String projectName, String projectRole) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectRole = projectRole;
    }

    @Override
    public String toString() {
        return String.format("Project ID: %s\n" +
                "Project Name: %s\n" +
                "Project Role: %s", this.projectId, this.projectName, this.projectRole
        );
    }
}
