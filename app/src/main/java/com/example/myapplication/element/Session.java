package com.example.myapplication.element;

public class Session {

    private static Session instance = new Session();
    private String userName;
    private Project currProject;

    private Session() {
    }

    public static Session getInstance() {
        return instance;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        currProject = new Project();
    }

    public void setCurrProject(Project project) {
        this.currProject = project;
    }

    public String getUserName() {
        return this.userName;
    }

    public Project getCurrProject() {
        return this.currProject;
    }

    public String getProjectName() {
        return this.currProject.projectName;
    }

    public String getProjectId() {
        return Long.toString(this.currProject.projectId);
    }
}
