package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.TaskAssignActivity;
import com.example.myapplication.element.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageTaskView {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    TaskAssignActivity activity;

    public ManageTaskView(TaskAssignActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Task");
        this.activity = activity;
    }

    public void getTaskList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            List<Task> taskList = new ArrayList<>();

                            for (DataSnapshot snap : snapshot.getChildren()) {
                                String taskId = snap.getKey();
                                String taskName = snap.child("task").getValue().toString();

                                DataSnapshot memberSnap = snap.child("memberList");
                                List<String> memberList = new ArrayList<>();
                                for (DataSnapshot tempSnap : memberSnap.getChildren()) {
                                    String member = tempSnap.getValue().toString();
                                    memberList.add(member);
                                }
                                Task tempTask = new Task(taskId, taskName, memberList);
                                taskList.add(tempTask);
                            }
                            activity.setupTaskView(taskList);
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void confirmRemove(final List<String> deleteList) {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String taskId : deleteList) {
                    db_ref.child(taskId).removeValue();
                }
                activity.reset();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
