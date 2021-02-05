package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Validation {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    boolean isExist;
    String username, projectId;
    String roles;


    public Validation(String username, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Roles");
        this.username = username;
        this.projectId = projectId;
        checkDatabase(username, projectId);
    }

    public String getRoles() {
        return this.roles;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }

    public boolean isExist() {
        return this.isExist;
    }

    private void checkDatabase(String username, String projectId) {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                isExist = snapshot.child(projectId).child(username).exists();
                System.out.println(username + ": " + isExist);
                if (isExist) {
                    roles = snapshot.child(projectId).child(username).child("Roles").getValue().toString();
                }
                setExist(isExist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("Return " + isExist);
    }
}
