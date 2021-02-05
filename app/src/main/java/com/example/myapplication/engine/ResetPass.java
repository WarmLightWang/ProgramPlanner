package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.ResetPassActivity;
import com.example.myapplication.element.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPass {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    ResetPassActivity activity;
    String username;

    public ResetPass(ResetPassActivity activity, String username) {
        this.firebase = FirebaseDatabase.getInstance();
        this.db_ref = firebase.getReference("Users");
        this.activity = activity;
        this.username = username;
    }

    public void resetPassword(String password) {
        if (!validatePassword(password)) {
            return;
        }
        String passHash = User.hashPassword(password);
        verifyHash(passHash);
    }

    private boolean validatePassword(String password) {
        boolean isValid = true;
        if (password.isEmpty() || password == null) {
            activity.setErrView("Please enter a password");
            isValid = false;
        }

        if (password.length() < 6) {
            activity.setErrView("Password Length must at least 6 characters");
            isValid = false;
        }
        return isValid;
    }

    private void verifyHash(final String passHash) {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.child(username).child("password").setValue(passHash);
                activity.finishReset();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
