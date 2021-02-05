package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.FeedbackActivity;
import com.example.myapplication.activity.ViewFeedbackActivity;
import com.example.myapplication.element.Feedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageFeedback {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    FeedbackActivity activity;
    ViewFeedbackActivity viewActivity;
    int feedbackId;
    Feedback newFeedback;

    public ManageFeedback(FeedbackActivity activity, String projectId) {
        this(projectId);
        this.activity = activity;
    }

    public ManageFeedback(ViewFeedbackActivity viewActivity, String projectId) {
        this(projectId);
        this.viewActivity = viewActivity;
    }

    public ManageFeedback(String projectId) {
        this.firebase = FirebaseDatabase.getInstance();
        this.db_ref = firebase.getReference("Project").child(projectId).child("feedback");
    }

    public void addFeedback(Feedback feedback) {
        this.newFeedback = feedback;
        generateFeedbackId();
        addFeedbackToDatabase();
    }

    public void addFeedbackToDatabase() {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.child(Integer.toString(feedbackId)).setValue(newFeedback);
                activity.finishSubmit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generateFeedbackId() {

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    feedbackId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFeedbackList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Feedback> feedbackList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String username = snap.child("username").getValue().toString();
                    String comment = snap.child("comment").getValue().toString();
                    Feedback tempFeed = new Feedback(username, comment);
                    feedbackList.add(tempFeed);
                }
                viewActivity.setupFeedbackList(feedbackList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
