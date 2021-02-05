package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.JoinProjectActivity;
import com.example.myapplication.element.Log;
import com.example.myapplication.element.Project;
import com.example.myapplication.element.Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinProject {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles, db_ref_users;
    JoinProjectActivity activity;
    boolean isValid, isDeveloper;
    String projectName, inviteCode, username, projectId, roles;
    List<Project> projectList;
    List<Application> appList;

    public JoinProject(JoinProjectActivity activity, String username) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project");
        db_ref_roles = firebase.getReference("Roles");
        db_ref_users = firebase.getReference("Users");
        this.activity = activity;
        this.username = username;
        retrieveDatabase();
        checkInvitation();
    }

    public void joinProject(String inviteCode, String username) {
        this.inviteCode = inviteCode;
        this.username = username;

        if (checkInviteCode(inviteCode)) {
            joinProjectInDatabase();
        }
    }

    public void applyProject(String projectName, String roles, String username) {
        this.projectName = projectName;
        this.roles = roles;
        this.username = username;

        if (checkProjectName(projectName)) {
            if (!isApplicationExist(username)) {
                appList.add(new Application(username, roles));
                //System.out.println("appList!!! " + appList);
                submitApplication();
            } else {
                activity.setErrText("Application already sent!");
            }
        }
    }

    private boolean isApplicationExist(String username) {
        for (Application app : appList) {
            if (app.username.equalsIgnoreCase(username))
                return true;
        }
        return false;
    }

    public void setProjectValue(Project project) {
        this.projectName = project.projectName;
        this.projectId = Long.toString(project.projectId);
        this.appList = project.applicationList;
    }

    private void retrieveDatabase() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList = new ArrayList<>();
                appList = new ArrayList<>();
                System.out.println("RETRIEVE DATABASE USING THIS METHOD");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String name = snap.child("projectName").getValue().toString();
                    long projectId = Long.parseLong(snap.getKey());
                    String clientCode = snap.child("clientCode").getValue().toString();
                    String devCode = snap.child("devCode").getValue().toString();
                    appList = new ArrayList<>();
                    for (DataSnapshot appSnap : snap.child("Application").getChildren()) {
                        String username = appSnap.child("username").getValue().toString();
                        String roles = appSnap.child("roles").getValue().toString();
                        appList.add(new Application(username, roles));
                    }

                    Project project = new Project(projectId, name, clientCode, devCode, appList);
                    projectList.add(project);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkInvitation() {
        db_ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean gotInvitation = snapshot.child(username).child("Invitation").exists();
                activity.setNotification(gotInvitation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private boolean checkInviteCode(String inviteCode) {
        boolean isExist = false;

        for (Project project : projectList) {
            if (project.clientCode.equals(inviteCode)) {
                activity.setProjectValue(project);
                setProjectValue(project);
                isValid = true;
                isDeveloper = false;
                isExist = true;
                break;
            } else if (project.devCode.equals(inviteCode)) {
                activity.setProjectValue(project);
                setProjectValue(project);
                isValid = true;
                isDeveloper = true;
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            activity.setErrText("The invitation code is invalid");
        }
        return isExist;
    }

    private boolean checkProjectName(String projectName) {
        System.out.println(projectList.toString());
        for (Project project : projectList) {
            if (project.projectName.equals(projectName)) {
                setProjectValue(project);
                return true;
            }
        }

        activity.setErrText("The project does not exist");

        return false;
    }

    public void joinProjectInDatabase() {

        db_ref_roles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isExist;
                if (isValid) {
                    isExist = snapshot.child(projectId).child(username).exists();
                    if (!isExist) {
                        String userRole = "client";
                        if (isDeveloper) {
                            userRole = "developer";
                        }

                        db_ref_roles.child(projectId).child("ProjectName").setValue(projectName);
                        db_ref_roles.child(projectId).child(username).child("Roles").setValue(userRole);

                        String logMessage = String.format("%s entered the invite code and joined the project team.", username);
                        Log log = new Log(logMessage, "SYSTEM");
                        new ManageLog(projectId, log);

                        activity.finishJoin(isDeveloper);
                    } else {
                        activity.setErrText("The user already join the project");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void submitApplication() {
        db_ref_project.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_project.child(projectId).child("Application").removeValue();
                db_ref_project.child(projectId).child("Application").setValue(appList);
                activity.finishApply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
