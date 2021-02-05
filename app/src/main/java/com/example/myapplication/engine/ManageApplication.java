package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.ViewApplicationActivity;
import com.example.myapplication.element.Application;
import com.example.myapplication.element.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageApplication {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles;
    ViewApplicationActivity activity;
    String projectId;
    List<Application> applicationList;

    public ManageApplication(ViewApplicationActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project");
        db_ref_roles = firebase.getReference("Roles");
        this.projectId = projectId;
        this.activity = activity;
    }

    public void acceptApplication(Application application) {
        String appId = application.appId;
        String username = application.username;
        String roles = application.roles;
        updateRoles(username, roles);
        deleteApplication(appId);
        String logMessage = String.format("%s's application is accepted and join the team.", username);
        Log log = new Log(logMessage, "SYSTEM");
        new ManageLog(projectId, log);
        activity.finishViewApplication("Application Accepted");
    }

    public void rejectApplication(Application application) {
        deleteApplication(application.appId);
        activity.finishViewApplication("Application Declined");
    }

    public List<Application> getApplicationList() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList = new ArrayList<>();
                snapshot = snapshot.child(projectId).child("Application");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String applicationId = snap.getKey();
                    String username = snap.child("username").getValue().toString();
                    String roles = snap.child("roles").getValue().toString();

                    Application tempApp = new Application(applicationId, username, roles);
                    applicationList.add(tempApp);
                }
                activity.setupLayout(applicationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return applicationList;
    }

    private void updateRoles(final String username, final String projectRole) {
        db_ref_roles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_roles.child(projectId).child(username).child("Roles").setValue(projectRole);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteApplication(final String appId) {
        db_ref_project.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_project.child(projectId).child("Application").child(appId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
