package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.CreateProjectActivity;
import com.example.myapplication.element.Log;
import com.example.myapplication.element.Project;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectCreate {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles;
    CreateProjectActivity activity;
    Project project;
    long projectId;
    String projectName;
    String username;
    Set<String> inviteCodeSet;
    List<Project> projectList;

    public ProjectCreate(CreateProjectActivity activity) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project");
        db_ref_roles = firebase.getReference("Roles");
        this.activity = activity;
        inviteCodeSet = new HashSet<>();
        projectList = new ArrayList<>();
        getAllCodes();
        retrieveProjectData();
        generateProjectId();
    }

    public String generateInviteCode(boolean isClient) {
        boolean isValid = false;
        String inviteCode;
        do {
            inviteCode = Project.generateCode(isClient);
            if (!inviteCodeSet.contains(inviteCode)) {
                inviteCodeSet.add(inviteCode);
                isValid = true;
            }
        } while (!isValid);
        return inviteCode;
    }

    public void getAllCodes() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    try {
                        System.out.println(snap.toString());
                        String clientCode = snap.child("clientCode").getValue().toString();
                        String devCode = snap.child("devCode").getValue().toString();

                        inviteCodeSet.add(clientCode);
                        inviteCodeSet.add(devCode);
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createProject(String projectName, String username) {
        this.username = username;
        this.projectName = projectName;
        if (projectName.isEmpty()) {
            activity.setErrView("Please enter a project name");
            return;
        }

        if (projectName.charAt(0) == ' ' || projectName.charAt(projectName.length() - 1) == ' ') {
            activity.setErrView("The first character or last character of " +
                    "the project name should not be space");
            return;
        }

        if (!isProjectExist(projectName)) {
            String clientCode = generateInviteCode(true);
            String devCode = generateInviteCode(false);
            project = new Project(projectId, projectName, clientCode, devCode);
            addProjectToDatabase();

            String logMessage = String.format("%s created the project.", username);
            Log log = new Log(logMessage, "SYSTEM");
            new ManageLog(Long.toString(projectId), log);
        }
    }

    private boolean isProjectExist(String projectName) {
        for (Project project : projectList) {
            if (project.projectName.equals(projectName)) {
                activity.setErrView("The project is already exist");
                return true;
            }
        }
        return false;
    }

    private void retrieveProjectData() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String projectId = snap.getKey();
                    String projectName = snap.child("projectName").getValue().toString();
                    projectList.add(new Project(Long.parseLong(projectId), projectName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generateProjectId() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    projectId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addProjectToDatabase() {
        db_ref_project.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_project.child(Long.toString(projectId)).setValue(project);
                activity.finishAddProject();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db_ref_roles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_roles.child(Long.toString(projectId)).child("ProjectName").setValue(projectName);
                db_ref_roles.child(Long.toString(projectId)).child(username).child("Roles").setValue("manager");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        activity.backToMain();
    }
}
