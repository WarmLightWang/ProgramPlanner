package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.ForgetPassActivity;
import com.example.myapplication.activity.ForgetQuesActivity;
import com.example.myapplication.element.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPass {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    ForgetPassActivity passActivity;
    ForgetQuesActivity quesActivity;
    String username;

    public ForgetPass(ForgetPassActivity passActivity, String username) {
        this();
        this.passActivity = passActivity;
        this.quesActivity = null;
        this.username = username;
        checkUsername();
    }

    public ForgetPass(ForgetQuesActivity quesActivity, String username) {
        this();
        this.quesActivity = quesActivity;
        this.passActivity = null;
        this.username = username;
    }

    public ForgetPass() {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Users");
    }

    public void checkUsername() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    boolean isExist = snapshot.child(username).exists();
                    if (isExist) {
                        passActivity.toForgetQuestion(username);
                    } else {
                        passActivity.setErrText("The username does not exist");
                    }
                } catch (NullPointerException exception) {
                    passActivity.setErrText("Unexpected error encountered. Please try again");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getQuestion() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String quesIndex = snapshot.child(username).child("questionIndex").getValue().toString();
                    int index = Integer.parseInt(quesIndex);
                    String question = Question.QUESTION[index];
                    quesActivity.setQuesView(question);
                } catch (NullPointerException exception) {
                    quesActivity.setErrText("Please enter a username");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void verifyQuestion(final String answer) {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String dbAns = snapshot.child(username).child("answer").getValue().toString();
                    if (answer.equals(dbAns))
                        quesActivity.finishVerify();
                    else
                        quesActivity.setErrText("Incorrect Security Answer");
                } catch (NullPointerException exception) {
                    quesActivity.setErrText("Value not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
