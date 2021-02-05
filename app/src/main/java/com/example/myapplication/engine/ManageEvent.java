package com.example.myapplication.engine;

import androidx.annotation.NonNull;

import com.example.myapplication.activity.EventActivity;
import com.example.myapplication.element.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageEvent {

    FirebaseDatabase firebase;
    DatabaseReference db_ref;
    EventActivity activity;
    int eventId;
    Event newEvent;

    public ManageEvent(EventActivity activity, String projectId) {
        firebase = FirebaseDatabase.getInstance();
        db_ref = firebase.getReference("Project").child(projectId).child("Event");
        this.activity = activity;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void getEventValue() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String eventIdStr = Integer.toString(eventId);
                boolean isExist = snapshot.child(eventIdStr).exists();
                if (isExist) {
                    snapshot = snapshot.child(Integer.toString(eventId));
                    String eventDate = snapshot.child("eventDate").getValue().toString();
                    String eventTitle = snapshot.child("eventTitle").getValue().toString();
                    boolean isNotify = Boolean.getBoolean(snapshot.child("isNotify").getValue().toString());

                    Event temp = new Event(eventIdStr, eventTitle, eventDate, isNotify);
                    activity.setEvent(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNewEvent(Event newEvent) {
        this.newEvent = newEvent;
        generateEventId();
        addFeedbackToDatabase();
    }

    public void editEvent(int eventId, Event event) {
        this.newEvent = event;
        this.eventId = eventId;
        addFeedbackToDatabase();
    }

    private void generateEventId() {
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    eventId = Integer.parseInt(snap.getKey()) + 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addFeedbackToDatabase() {
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                db_ref.child(Integer.toString(eventId)).setValue(newEvent);
                activity.finishAdd();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
