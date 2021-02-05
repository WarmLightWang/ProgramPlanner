package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.TimelineActivity;
import com.example.myapplication.element.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageTimeline {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    TimelineActivity activity;

    public ManageTimeline(TimelineActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Event");
        this.activity = activity;

    }

    public void getEventList() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String eventId = snap.getKey();
                    String date = snap.child("eventDate").getValue().toString();
                    String eventTitle = snap.child("eventTitle").getValue().toString();
                    boolean isNotify = Boolean.getBoolean(snap.child("isNotify").getValue().toString());

                    Event temp = new Event(eventId, eventTitle, date, isNotify);
                    eventList.add(temp);
                }
                Collections.sort(eventList, new Event());
                activity.setupEventList(eventList);
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
                for (String eventId : deleteList) {
                    db_ref.child(eventId).removeValue();
                }
                activity.reset();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
