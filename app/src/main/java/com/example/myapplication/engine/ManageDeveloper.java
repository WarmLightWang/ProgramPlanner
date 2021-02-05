package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.DeveloperActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageDeveloper {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles;
    DeveloperActivity activity;
    String projectId;

    public ManageDeveloper(DeveloperActivity activity) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project");
        db_ref_roles = firebase.getReference("Roles");
        this.activity = activity;
    }

    public void removeData(String projectId) {
        this.projectId = projectId;
        removeProjectData();
        removeRolesData();
        removeProjectInvite();
        System.out.println("Check delete");
        activity.finishDelete();
    }

    private void removeProjectData() {
        db_ref_project.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_project.child(projectId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void removeRolesData() {
        db_ref_roles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref_roles.child(projectId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void removeProjectInvite() {
        final DatabaseReference db_ref_user = firebase.getReference("Users");
        db_ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String username = snap.getKey();
                    for (DataSnapshot inviteSnap : snap.child("Invitation").getChildren()) {
                        String inviteId = inviteSnap.getKey();
                        String tempId = inviteSnap.child("projectId").getValue().toString();
                        if (projectId.equals(tempId)) {
                            db_ref_user.child(username).child("Invitation").child(inviteId).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
