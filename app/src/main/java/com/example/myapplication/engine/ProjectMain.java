package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.ProjectMainActivity;
import com.example.myapplication.element.Roles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectMain {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    ProjectMainActivity activity;
    String username;
    List<Roles> projectList;

    public ProjectMain(ProjectMainActivity activity, String username) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Roles");
        this.username = username;
        this.activity = activity;
    }

    public void getProjectList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    boolean isExist = snap.child(username).exists();

                    if (isExist) {
                        String projectName = snap.child("ProjectName").getValue().toString();
                        String projectId = snap.getKey();
                        String role = snap.child(username).child("Roles").getValue().toString();
                        Roles tempRoles = new Roles(projectId, projectName, role);
                        projectList.add(tempRoles);
                    }
                }
                activity.setupProjectList(projectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
