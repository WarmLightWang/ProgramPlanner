package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.SignUpActivity;
import com.example.myapplication.element.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    SignUpActivity activity;
    User user;

    public SignUp(SignUpActivity activity, User user) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Users");
        this.activity = activity;
        this.user = user;
        signUp();
    }

    public void signUp() {
        if (!validatePassword(user.password)) {
            return;
        }
        user.password = User.hashPassword(user.password);
        if (user.password == null) {
            activity.setErrView("Encountered unexpected error");
        } else {
            updateDatabase();
        }
    }

    private boolean validatePassword(String password) {
        if (password == null || password.length() < 6 || password.isEmpty()) {
            activity.setErrView("Password Length must at least 6 characters");
            return false;
        }
        return true;
    }

    private void updateDatabase() {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.username).exists()) {
                    activity.setErrView("Username already exists!");
                } else {
                    db_ref.child(user.username).setValue(user);
                    activity.finishSignUp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
