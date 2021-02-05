package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.AddTaskActivity;
import com.example.myapplication.element.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageTask {

    FirebaseDatabase firebase;
    DatabaseReference db_ref_project, db_ref_roles;
    AddTaskActivity activity;
    Task task;
    int taskId;

    public ManageTask(AddTaskActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref_project = firebase.getReference("Project").child(projectId).child("Task");
        db_ref_roles = firebase.getReference("Roles").child(projectId);
        this.activity = activity;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void getTaskValue() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String taskIdStr = Integer.toString(taskId);
                String title = snapshot.child(taskIdStr).child("task").getValue().toString();
                List<String> memberList = new ArrayList<>();

                DataSnapshot memberSnap = snapshot.child(taskIdStr).child("memberList");
                for (DataSnapshot snap : memberSnap.getChildren()) {
                    String member = snap.getValue().toString();
                    memberList.add(member);
                }
                Task temp = new Task(taskIdStr, title, memberList);
                activity.setTask(temp, memberList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMemberList() {
        db_ref_roles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    List<String> memberList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        String member = snap.getKey();
                        boolean isMember = snap.child("Roles").exists();

                        if (isMember) {
                            String role = snap.child("Roles").getValue().toString();
                            if (role.equals("developer")) {
                                memberList.add(member);
                            }
                        }
                    }
                    activity.setupSpinner(memberList);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNewTask(Task task) {
        this.task = task;
        generateTaskId();
        addTaskToDatabase();
    }

    public void editTask(Task task, int taskId) {
        this.task = task;
        this.taskId = taskId;
        addTaskToDatabase();
    }

    public void generateTaskId() {
        db_ref_project.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        taskId = Integer.parseInt(snap.getKey()) + 1;
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addTaskToDatabase() {
        db_ref_project.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    db_ref_project.child(Integer.toString(taskId)).setValue(task);
                    activity.finishAddTask();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
