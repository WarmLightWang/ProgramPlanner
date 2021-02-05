package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.LogViewActivity;
import com.example.myapplication.element.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageLog {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    LogViewActivity activity;
    Log log;
    int logId;

    public ManageLog(LogViewActivity activity, String projectId) {
        this(projectId);
        this.activity = activity;
    }

    public ManageLog(String projectId, Log log) {
        this(projectId);
        addLog(log);
    }

    public ManageLog(String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Log");
    }

    public void getLogList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Log> logList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String date = snap.child("date").getValue().toString();
                    String content = snap.child("content").getValue().toString();
                    String username = snap.child("username").getValue().toString();

                    Log tempLog = new Log(date, content, username);
                    logList.add(tempLog);
                }
                activity.setupLogList(logList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addLog(Log log) {
        this.log = log;
        generateLogId();
        addLogToDatabase();
    }

    private void generateLogId() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    logId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addLogToDatabase() {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.child(Integer.toString(logId)).setValue(log);
                if (activity != null)
                    activity.finishAddLog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
